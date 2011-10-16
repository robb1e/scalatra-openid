package com.iclutton

import org.scalatra._
import org.openid4java.consumer._
import org.openid4java.discovery._
import org.openid4java.message.ax._
import org.openid4java.message._

case class AuthUser(email: String, firstName: String, lastName: String)

class AuthFilter extends ScalatraFilter {
    
    var sessionAuth: Map[String, AuthUser] = Map()
    val manager = new ConsumerManager
    
    get("/") {
        <ul>
            <li>openid4java</li>
            <ul>
                <li><a href="/openid4java/google">Google</a></li>
            </ul>
        </ul>
    }
    
    get("/logout") {
        sessionAuth.get(session.getId) match {
            case Some(user) => sessionAuth = sessionAuth - session.getId
            case None => 
        }
        redirect("/")
    }
    
    get("/openid4java/google") {
        
        sessionAuth.get(session.getId) match {
            case None => {        
                val discoveries = manager.discover("https://www.google.com/accounts/o8/id")
                val discovered = manager.associate(discoveries)
                session.setAttribute("discovered", discovered)
                val authReq = manager.authenticate(discovered, "http://localhost:8080/openid4java/google/authenticated")
                val fetch = FetchRequest.createFetchRequest()
                fetch.addAttribute("email", "http://schema.openid.net/contact/email",true)
                fetch.addAttribute("firstname", "http://axschema.org/namePerson/first", true)
                fetch.addAttribute("lastname", "http://axschema.org/namePerson/last", true)
                authReq.addExtension(fetch)
                redirect(authReq.getDestinationUrl(true))        
            }
            case Some(user) => {
                val person = "Hello %s %s".format(user.firstName, user.lastName) 
                <div><p>{person}</p><p><a href="/logout">logout</a></p></div>
            }
        }
    }
    
    get("/openid4java/google/authenticated") {
        val openidResp = new ParameterList(request.getParameterMap())
        val discovered = session.getAttribute("discovered").asInstanceOf[DiscoveryInformation]
        val receivingURL = request.getRequestURL()
        val queryString = request.getQueryString()
        if (queryString != null && queryString.length() > 0)
            receivingURL.append("?").append(request.getQueryString())

        val verification = manager.verify(receivingURL.toString(), openidResp, discovered)
        val verified = verification.getVerifiedId()
        if (verified != null) {
          val authSuccess = verification.getAuthResponse().asInstanceOf[AuthSuccess]
          if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)){
            val fetchResp = authSuccess.getExtension(AxMessage.OPENID_NS_AX).asInstanceOf[FetchResponse]
            val emails = fetchResp.getAttributeValues("email")
            val email = emails.get(0).asInstanceOf[String]
            val firstName = fetchResp.getAttributeValue("firstname")
            val lastName = fetchResp.getAttributeValue("lastname")
            sessionAuth += (session.getId -> AuthUser(email, firstName, lastName))
            redirect("/openid4java/google")
          }
        } else
          "not verified"        
    }
    
}