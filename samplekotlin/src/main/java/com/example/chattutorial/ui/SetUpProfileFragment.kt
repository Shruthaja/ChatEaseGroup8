package com.example.chattutorial.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chattutorial.ChannelActivity4
import com.example.chattutorial.MainActivity
import com.example.chattutorial.R
import com.example.chattutorial.databinding.ActivityMainBinding
import com.example.chattutorial.databinding.FragmentSetupProfileBinding
import com.google.android.gms.common.api.Api.Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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


class SetUpProfileFragment : Fragment(R.layout.fragment_setup_profile) {
    private lateinit var binding: FragmentSetupProfileBinding
    private lateinit var currentUser: FirebaseUser
    private lateinit var client: ChatClient  // Declare the client at the class level
    private lateinit var binding1: ActivityMainBinding

    // Step 1 - Set up the OfflinePlugin and StatePlugin factories
//    private val offlinePluginFactory by lazy { StreamOfflinePluginFactory(appContext = requireContext()) }
//    private val statePluginFactory by lazy {
//        StreamStatePluginFactory(
//            config = StatePluginConfig(
//                backgroundSyncEnabled = true,
//                userPresence = true,
//            ),
//            appContext = requireContext(),
//        )
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)
        binding.buttonNext.isEnabled = false
        binding.editTextName.addTextChangedListener {
            binding.buttonNext.isEnabled = it?.isNotEmpty() == true
        }

        currentUser = FirebaseAuth.getInstance().currentUser ?: return

        binding.buttonNext.setOnClickListener {
            setupProfile()
        }
    }

    private fun setupProfile() {
//        val user = User(
//            id = binding.editTextName.text.toString().trim(),
//            extraData = mutableMapOf(
//                "name" to binding.editTextName.text.toString().trim(),
//                "image" to "https://bit.ly/321RmWb",
//            ),
//        )

//        val token = client.devToken(user.id)
//
//        Log.d("token ===========>   ", token.toString())

//        client.connectUser(
//            user = user,
//            token = token
//        ).enqueue {
//            if (it.isSuccess) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("userName", binding.editTextName.text.toString().trim())
                intent.putExtra("userId", binding.editTextName.text.toString().trim())
                startActivity(intent)//                Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
//            }
//        }
        // Rest of the code...
    }
}
