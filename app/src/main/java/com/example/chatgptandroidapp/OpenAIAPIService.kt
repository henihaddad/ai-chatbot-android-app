//import com.example.chatgptandroidapp.ChatRequestBody
//import com.example.chatgptandroidapp.Message
import com.example.chatgptandroidapp.ChatRequestBody
import com.example.chatgptandroidapp.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



//data class ChatRequestBody(val prompt: String)
interface OpenAIApiService {
    @POST("chat/completions")
    fun postMessage(@Body requestBody: ChatRequestBody): Call<ChatResponse>
}

data class ChatResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)


