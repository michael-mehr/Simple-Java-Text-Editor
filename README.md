# Simple-Java-Text-Editor

Fork of [PH NotePad/Simple Java Text Editor](https://github.com/pH-7/Simple-Java-Text-Editor)

Light weight text editor built in Java

## Features

- Search tool
  - search for text/keywords in file
  - highlighting found text
- Find/Replace text
- Auto completion for Java and C++ keywords (files need to be saved as _.java_/_.cpp_). It can be easily expanded to support pretty much any number of languages.
- Drag and Drop
  - drag files into the text area and they get loaded
- Image buttons
- Customizable UI

## Demo/Usage

To build and then open the `.jar`, run the following:

```bash
jar cfm SimpleJavaTextEditor.jar manifest.mf -C bin .
java -jar SimpleJavaTextEditor.jar
```

## Acknowledgments

- Pierre-Henry Soria (Original Author) + contributors to his repo
- Dr. Peter Lars Dordal
- Boxicons
