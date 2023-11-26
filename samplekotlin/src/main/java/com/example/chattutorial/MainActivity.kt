package com.example.chattutorial
import android.R.style.Theme
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chattutorial.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.chattutorial.ui.AuthActivity
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModelFactory
import io.getstream.chat.android.ui.viewmodel.channels.bindView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        toggleDarkMode()
        super.onCreate(savedInstanceState)

        // Step 0 - inflate binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val userId = intent.getStringExtra("userId")


        if(userName == null && userId == null){
            startActivity(Intent(this, AuthActivity::class.java))
        } else {
            val offlinePluginFactory = StreamOfflinePluginFactory(appContext = this)
            val statePluginFactory = StreamStatePluginFactory(
                config = StatePluginConfig(
                    backgroundSyncEnabled = true,
                    userPresence = true,
                ),
                appContext = this,
            )


            // Step 2 - Set up the client for API calls with the plugin for offline storage
            val client = ChatClient.Builder("f7ypjdv2usn9", applicationContext)
                .withPlugins(offlinePluginFactory, statePluginFactory)
                .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
                .build()
            Toast.makeText(this, client.toString(), Toast.LENGTH_SHORT).show()
            // Step 3 - Authenticate and connect the user
            val user = User(
                id = userId.toString(),
                name = userName.toString(),
                image = ""
            )
//        client.searchMessages()
            val t=client.devToken(userId = userId.toString())
//        print(user)
            client.connectUser(
                user = user,
                token = t
            ).enqueue {
                if (it.isSuccess) {
//                startActivity(Intent(this, AuthActivity::class.java))
                    // Step 4 - Set the channel list filter and order
                    // This can be read as requiring only channels whose "type" is "messaging" AND
                    // whose "members" include our "user.id"
                    val filter = Filters.and(
                        Filters.eq("type", "messaging"),
                        Filters.`in`("members", listOf(user.id))
                    )
                    val viewModelFactory =
                        ChannelListViewModelFactory(filter, ChannelListViewModel.DEFAULT_SORT)
                    val viewModel: ChannelListViewModel by viewModels { viewModelFactory }

                    // Step 5 - Connect the ChannelListViewModel to the ChannelListView, loose
                    //          coupling makes it easy to customize
                    viewModel.bindView(binding.channelListView, this)
                    binding.channelListView.setChannelItemClickListener { channel ->
                        startActivity(ChannelActivity4.newIntent(this, channel))
//                    startActivity(Intent(this, AuthActivity::class.java))

                    }
                } else {
                    Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }


            val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
            fab.setOnClickListener { view ->
                val intent = Intent(this, channelmanage::class.java)
                startActivity(intent)

            }
        }













        }
    }

