# Copy
A program for moving files between folders, with the ability to set paths, multiple extensions and compare the length of characters in the file name.

## Configuration
This program is configurated via config.ini file (included in the project files).
The config.ini file must be in the same path as .jar file.

### config.ini
There are 5 configuration fields:
* SourceDirection
  - It must not be empty.
  - Enter there the full path of directory from which files should be moved.
  - If there is a backslash '\' in the path, you need to enter two backslashes instead of one. (For the network paths it should start like this: \\\\192.168.0.1\\directory)
* DestinationDirection
  - The same as above, but for the target directory
* Extensions
  - This field is stored as table.
  - The extensions should be wrote one after one, separated with comma (eg. Extensions=xml,pdf,txt)
* LengthComparator
  - This field is stored as a Compare enum
  - Possible options are: LESS, LESSEQUAL, EQUAL, GREATEREQUAL, GREATER
  - This is comparing file name to the FileLength field from this file (eg. for LESSEQUAL: filename.length() <= 10)
  - The comparision takes place only on the file name (without the extension)
* FileLength
  - Stored as integer
  - Can not be less than 0

 
### How to move every file?
To move every file the config.ini should look like this:
```
[Application]
SourceDirection=C:\\source\\directory
DestinationDirection=C:\\some\\other\\directory
Extensions=*
LengthComparator=greaterequal
FileLength=0
```
