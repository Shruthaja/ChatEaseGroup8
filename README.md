# Android Chat Tutorial Sample

This repository contains Android Chat Tutorial samples in both Kotlin (samplekotlin module) and Java (samplejava module). For more examples, visit the Stream Chat SDK GitHub repository and the UI Components sample app.

# For Jetpack Compose Users:
If you're using Jetpack Compose, refer to the Compose UI Components tutorial repository.

# Prerequisites
This project is pre-configured with a shared Stream account for testing purposes. For more information about Stream Chat and to sign up for your own account, visit Stream Chat.

# Quick Start -
1. Clone this repository:
2. git clone https://github.com/your-repo/android-chat-sample.git
3. Open the project in Android Studio.
4. Run either the samplekotlin or samplejava configuration in your emulator or physical device.
5. See the Details section below for further customizations.

# The sample apps have two main screens:

1. MainActivity: Displays the list of available chat channels.
2. ChannelActivity: Displays the selected channel, including the header, message list, and input view.
Each module includes multiple ChannelActivity implementations based on tutorial steps. You can switch between them by updating the setOnChannelClickListener in MainActivity:

# kotlin

channelListView.setOnChannelClickListener { channel ->
    // Open the channel activity
    startActivity(ChannelActivity.newIntent(this, channel))
}

# You can choose from the following ChannelActivity implementations:

1. ChannelActivity: Basic message list implementation.
2. ChannelActivity2: Includes a new MessageListView style and custom attachment types.
3. ChannelActivity4: Features a custom typing header component created with the low-level client library.

# Customization
You can modify or enhance the chat experience by following the steps provided in the tutorial. Make sure to explore the different implementations in the project for learning and customization opportunities.
