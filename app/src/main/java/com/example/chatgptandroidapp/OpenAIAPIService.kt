import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIApiService {
    @POST("engines/davinci-codex/completions") // Replace with the correct endpoint
    fun postMessage(@Body requestBody: ChatRequestBody): Call<ChatResponse>
}

data class ChatRequestBody(val prompt: String)
data class ChatResponse(val choices: List<Choice>)
data class Choice(val text: String)
