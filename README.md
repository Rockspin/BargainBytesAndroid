## Bargain Bytes Android App
Repository for the Open Source Bargain Bytes Project

- [Play Store Link](https://play.google.com/store/apps/details?id=com.rockspin.bargainbits)

### What is Bargain Bytes?

An Android application which helps PC gamers keep track of game sales happening across a number of online retailers like Steam, GoG, Gamersgate, etc...

It does this by using the [Cheapshark API](http://www.cheapshark.com/api/)

#### History

The project was originally started by Rockspin in 2014 - namely [ValCanBuild](https://github.com/ValCanBuild) and [RoryKelly](https://github.com/RoryKelly) 
It was in continous development and improvement upto the beginning of 2016 but since then it's not seen any new improvements.
Despite this, the app maintains a high number of active installs and has reached 35k total installs. This is the main reason it is being open sourced. If maintained and improved, its user base can grow and the existing users will benefit.

#### Current High-Level Features
- View top game deals by type (release date, score, price, saving)
- Search for a specific game to see if it has any ongoing sales.
- Filter which online stores you want to see games from
- Add games to a Watch List and get a notification when they go on sale.

### Plan going forward

#### Step 1 - Refactor App, Establish Architecture & Write Tests.
In order of importance - but some tasks can be done simultaneously. All of the existing Java code in the app is up for grabs. 
1. Convert project to Kotlin - This will be done incrementally. Taking one screen, one class at a time, improving the project architecture as it goes along.
2. Rework the architecture to MVP (Model-View-Presenter). Some screens in the app already *try* to follow this pattern but not well. Once one screen is converted to use the new MVP architecture it can be used as an example to others.
3. Add Unit & Integration testing to the project. In the beginning AndroidFramework-independant unit tests will suffice. Presenters and Business Logic should aim for high code coverage. This will be followed up by Espresso integration testing.
4. Add proper configuration-changes support to screens. The app currently uses the android:configChanges attribute for all screens, which is bad.
5. Setup Travis CI to run tests and generate APKs.
6. Reduce number of library dependencies where possible and update existing ones.
7. Add Checkstyles support to enforce coding standard.

#### Step 2 - Work on new features. 
Each feature will be described in more detail in its own ticket. This is just an overview. In no particular order.
* Open deal links in Custom Chrome Tabs in-app instead of an external browser.
* Fetch store images from the Cheapshark API instead of having them in the app.
* Redesign search as a floating action button on main screen. 
* Smart search - Searching should dynamically populate the results.
* Add "add to watch list" as custom Chrome Tab option when opening deals.
* Consolidate analytics & crash reporting tool into one. Use either Fabric or Firebase.
* Better caching. A lot of what the app fetches can be cached to allow it to work offline.
* Paging of deal list - Automatically load new results once list is scrolled to the bottom.
* Redesign of deal list - Need idea
* Redesign of store filter - Need idea
* Redesign of store picker once a deal with multiple stores is selected - Need idea
* Any other ideas? - Please open a PR to suggest a new one if you think it'll fit the app.

#### Any bugs?
If you find a bug - please open an issue. Until the list in Step 1 is complete, expect a few, but please list anything that hasn't already been mentioned.

#### Are you a designer?
If you're a designer why not reach out? The app needs some love in the design department as seen from Step 2 - large parts of its UI haven't been updated since originally created. If you'd like to offer UI help, please open an issue.
