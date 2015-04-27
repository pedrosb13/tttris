## 1) Check if your language already exists ##

First, you need the code of your language ([here](http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) a list).

Then go to http://code.google.com/p/tttris/source/browse/trunk/res/ and look for a folder called _values-xx_ (where _xx_ is the language code)

If tat folder exists, the game is already translated to that language. But you may also want to update translation.


## 2) Translating the file ##

Download the most up-to-date English file from [here](https://code.google.com/p/tttris/source/browse/trunk/res/values/strings.xml) (if you have checked out the source code this is not necessary.
Translate the file into your language. It's important to save the file in UTF-8 format, due to language-specific characters.

## 3) Submit the file ##

  * If you have checked out the full source code, you can sumbit a revision. To do that, you  have to create a folder called _values-xx_ (where, again, _xx_ is the language code) and save the file inside as _strings.xml_ (no _strings-xx.xml_). After that, use your favourite tool to upload changes. Please indicate in the comment that you have translated the app.

  * If you just downloaded the strings file, you can send it by email to seavenois@gmail.com. Please indicate in the subject something like _"Tttris translation - xx"_. Also indicate your name (real or user name), so we can add you to the credits.