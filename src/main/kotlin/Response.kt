import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson

@JsonDeserialize
data class Post(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("userId")
    val userId: Int,
    @JsonProperty
    val title: String,
    @JsonProperty
    val body: String
) {
    class Deserializer : ResponseDeserializable<Array<Post>> {
        override fun deserialize(content: String): Array<Post>? {
            return Gson().fromJson(content, Array<Post>::class.java)
        }
    }
}

fun main(args: Array<String>) {
    // Sync call
    val (request1, response1, result1) = Fuel.post(
        "http://httpbin.org/post"
    ).responseString()

    val (payload1, error1) = result1

    println("Payload: $payload1")
    println("Error: $error1")

    // Async call
    "https://jsonplaceholder.typicode.com/posts".httpGet()
        .responseObject(Post.Deserializer()) {
        _,_, result2 ->
        val postsArray = result2.component1()
        println("PostsArray: ${postsArray?.size}")
        println("First Post: ${postsArray?.get(0)}")
    }
    Thread.sleep(3000)
}