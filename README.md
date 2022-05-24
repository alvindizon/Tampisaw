# Tampisaw

## About
 * An Android app that allows users to set wallpapers and download them via [Unsplash](https://unsplash.com/)
 * Uses Android Jetpack libraries (ViewModel, Paging 3, Navigation, WorkManager), Hilt for dependency injection,
  and RxJava 3 for async tasks
 * Uses JUnit5 and MockK for unit tests
 * For CI, Github Actions is used
 * Detekt is used for detecting code smells, alongside with lint
 * Features are separated into their own separate module

 ## Screens
| Gallery      | Collections | Search       |
| -------------|-------------| -------------|
| <img src="./art/gallery.gif"/> | <img src="./art/collections.gif"/> | <img src="./art/search.gif"/> |

## Features
 * Download wallpapers straight to your phone
 * Set your phone's wallpaper within the app
 * Discover photo collections
 * Search for photos and collections

## How to build
 1. You need to obtain an Unsplash access key in order to interact with the Unsplash API. Once you've done that, in your
 `local.properties` file add the following line:

 ```
 ACCESS_KEY=<your_unsplash_key>
 ```
 2. You also need to have a Firebase project set up so that you can place its `google-services.json` file in the `app/` directory.


