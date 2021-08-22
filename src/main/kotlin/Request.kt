import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet

fun main(args: Array<String>) {
    // Fuel supplies a singleton object to handle global configurations
    // Be careful, using these declarations will apply the same attributes for all requests
    FuelManager.instance.basePath = "https://jsonplaceholder.typicode.com"
    FuelManager.instance.baseHeaders = mapOf("Test" to "Header")

    fun tokenInterceptor() = {
        next: (Request) -> Request ->
        {
            req: Request ->
            req.header(mapOf("TokenI" to "Interceptor*"))
            next(req)
        }
    }

    FuelManager.instance.addRequestInterceptor(tokenInterceptor())

    val postsUrl = "/posts"
    val getResult = postsUrl.httpGet(
        listOf("userId" to "1")
    ).response()

    println("Printing getResult:")
    println(getResult)

    val postResult = Fuel.post(postsUrl).response() // It's similar to postsUrl.httpPost()

    println("Printing postResult:")
    println(postResult)

    val body = object {
        val title = "Post Title"
        val body = "This is the body of the post"
        val id = "999"
    }
    val jsonBody = ObjectMapper().writeValueAsString(body)
    val postResultWithBody = Fuel.post(postsUrl).body(jsonBody).response()

    println("Printing postResultWithBody:")
    println(postResultWithBody)
}