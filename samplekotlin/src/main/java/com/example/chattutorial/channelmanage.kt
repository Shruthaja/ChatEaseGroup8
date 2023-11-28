package com.example.chattutorial

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import java.util.UUID

class channelmanage : AppCompatActivity() {

    private fun showNameInputDialog() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.channelmanage)
        val request = QueryUsersRequest(
            filter = Filters.`in`("id", listOf("john", "jack", "jessie")),
            offset = 0,
            limit = 3,
        )
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

        val t=client.devToken(userId = "student121")

        client.connectUser(
            user = user,
            token = t
        ).enqueue {
            client.queryUsers(request).enqueue { result ->
                if (result.isSuccess) {
                    val request = QueryUsersRequest(
                        filter = Filters.neutral(),
                        offset = 0,
                        limit = 30,
                    )

                    client.queryUsers(request).enqueue { result ->

                        if (result.isSuccess) {
//                            val textView = findViewById(R.id.textView) as TextView
                            val users: List<User> = result.getOrThrow()
                            val result= mutableListOf<String>()
                            for (user in users){
                               result.add(user.id)
                            }
                            Log.d("Sample",result.toString())
                            val listView = findViewById<ListView>(R.id.listview)
                            val searchView = findViewById<SearchView>(R.id.searchView)
                            val actionButton=findViewById<Button>(R.id.button)
                            val adapter = CustomArrayAdapter(this@channelmanage, android.R.layout.simple_list_item_1, result)
                            listView.adapter=adapter
                            val selectedItems = mutableListOf<String>()


                            listView.setOnItemClickListener { _, _, position, _ ->
                                listView.setItemChecked(position, true)
                                val selectedItem = adapter.getItem(position) ?: return@setOnItemClickListener
                                adapter.toggleSelection(selectedItem)
                                if (selectedItems.contains(selectedItem)) {
                                    selectedItems.remove(selectedItem)
                                } else {
                                    selectedItems.add(selectedItem)
                                }
                            }

                             //Handle button click to perform actions on selected items
                            actionButton.setOnClickListener {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("Enter Group Name")

                                val input = EditText(this)
                                builder.setView(input)

                                builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                                    val name = input.text.toString()
                                    if (name.isNotEmpty()) {
                                        // Perform actions with the entered name
                                        // For example, you can use it in the channel creation logic
//                Toast.makeText(this, "Entered Name: $name", Toast.LENGTH_SHORT).show()

                                        if (selectedItems.isNotEmpty()) {
                                            // Example: Display a Toast with selected items
                                            val selectedItemsText = selectedItems.joinToString(", ")
                                            //actions here based on selected items
                                            Toast.makeText(this, "Selected Items: $selectedItemsText", Toast.LENGTH_SHORT).show()
                                            if(selectedItems.size==1){
                                                selectedItems.add("student121")
                                                val channelClient = client.channel(channelType = "messaging", channelId = selectedItems[0])
                                                channelClient.create(memberIds = selectedItems,extraData = mapOf("name" to name)).enqueue { result ->
                                                    if (result.isSuccess) {
                                                        val channel: Channel = result.getOrThrow()
                                                        Log.d("channelid",channel.toString())
                                                        startActivity(ChannelActivity3.newIntent(this,channel))
                                                    } else {
                                                        // Handle result.error()
                                                    }
                                                }
                                            }

                                            else {
                                                val channelClient = client.channel(
                                                    channelType = "messaging",
                                                    channelId = UUID.randomUUID().toString()
                                                )

                                                channelClient.create(
                                                    memberIds = selectedItems,
                                                    extraData = mapOf(
                                                        "name" to name
                                                    )
                                                ).enqueue { result ->
                                                    if (result.isSuccess) {
                                                        val channel: Channel = result.getOrThrow()
                                                        Log.d("channelid", channel.toString())
                                                        startActivity(
                                                            ChannelActivity4.newIntent(
                                                                this,
                                                                channel
                                                            )
                                                        )
                                                    } else {
                                                        // Handle result.error()
                                                    }
                                                }
                                            }


                                            //end of group creation or songle chat logic
                                        } else {
                                            Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show()
                                        }


                                    } else {
                                        Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                                    }
                                    dialog.dismiss()
                                }

                                builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                                    dialog.dismiss()
                                }

                                builder.show()
//                                showNameInputDialog()
                                // Perform actions using the selected items
                            }

                            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    adapter.filter.filter(newText)
                                    return false
                                }
                            })
//
                        } else {
                            Toast.makeText(this@channelmanage, "something went wrong!", Toast.LENGTH_SHORT).show()
                        }
                        } }


                }
            }
        }

    }
class CustomArrayAdapter(
    context: Context,
    resource: Int,
    objects: List<String>
) : ArrayAdapter<String>(context, resource, objects) {

    private val selectedItems = mutableListOf<String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        // Check if the item is in the selectedItems list
        if (selectedItems.contains(getItem(position))) {
            // If selected, change the background color (you can customize this)
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        } else {
            // If not selected, set the default background color (you can customize this)
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }

        return view
    }

    fun toggleSelection(item: String) {
        // Toggle the selection state of the item
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }

        notifyDataSetChanged() // Update the view to reflect the selection changes
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged() // Update the view to clear all selections
    }
}


