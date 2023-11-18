package com.example.chattutorial
import android.R.style.Theme
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.chattutorial.databinding.ActivityMainBinding
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

        // Step 1 - Set up the OfflinePlugin for offline storage
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
            id = "student121",
            name = "student121",
            image = ""
        )
//        client.searchMessages()
        val t=client.devToken(userId = "student121")
//        print(user)
        client.connectUser(
            user = user,
            token = t
        ).enqueue {
            if (it.isSuccess) {
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
                }
            } else {
                Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleDarkMode() {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK

        // If the current mode is night mode, switch to light mode; otherwise, switch to dark mode
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Recreate the activity to apply the new mode
        recreate()
    }

}
