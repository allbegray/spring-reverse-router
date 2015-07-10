package kr.redo.reverseRouter

import kr.redo.reverseRouter.kotlin.redirect
import kr.redo.reverseRouter.kotlin.redirectFor
import kr.redo.reverseRouter.kotlin.reverseRouter
import kr.redo.reverseRouter.kotlin.urlFor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import javax.inject.Inject
import kotlin.properties.Delegates

Controller
class MainController {
    RequestMapping("/")
    fun index() {
    }
}

Controller
RequestMapping("/user")
class UserController {
    RequestMapping("/")
    fun list() {
    }

    RequestMapping("/{id}")
    fun show() {
    }

    RequestMapping("/{id}/edit", "/new/edit")
    fun edit() {
    }
}

Configuration
ComponentScan
EnableWebMvc
open class WebMvcConfig : WebMvcConfigurerAdapter() {
    Bean open fun reverseRouter(): ReverseRouter = reverseRouter

    Bean open fun applicationListener(): ApplicationListener<*> = reverseRouter()
}


RunWith(SpringJUnit4ClassRunner::class)
ContextConfiguration(classes = arrayOf(WebMvcConfig::class))
WebAppConfiguration
class ReverseRouterTest {
    Inject
    private var wac: WebApplicationContext? = null
    private var mockMvc by Delegates.notNull<MockMvc>()

    Before
    public fun before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }


    Test
    fun testInitialize() {
        Assert.assertTrue(reverseRouter.initialized)
    }

    Test
    fun testIndex() {
        Assert.assertEquals("/", urlFor("main.index"))
    }

    Test
    fun testUser() {
        Assert.assertEquals("/user/", urlFor("user.list"))
        Assert.assertEquals("/user/12", urlFor("user.show", "id" to 12))

        Assert.assertEquals("/user/new/edit", urlFor("user.edit"))
        Assert.assertEquals("/user/12/edit", urlFor("user.edit", "id" to 12))
    }

    Test
    fun testHandleNullable() {
        Assert.assertEquals("/user/new/edit", urlFor("user.edit", "id" to null))
    }

    Test
    fun testExtraParams() {
        Assert.assertEquals("/user/?page=10", urlFor("user.list", "page" to 10))
    }

    Test
    fun testRedirect() {
        Assert.assertEquals("redirect:/user/", redirect(urlFor("user.list")).getViewName())
    }

    Test
    fun restRedirectFor() {
        Assert.assertEquals("redirect:/user/", redirectFor("user.list").getViewName())
    }

}
