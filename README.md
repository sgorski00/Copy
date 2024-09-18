# Copy
A program for moving files between folders, with the ability to set paths, multiple extensions and compare the length of characters in the file name.

## Configuration
This program is configurated via config.ini file (included in the project files).
The config.ini file must be in the same path as .jar file.

### config.ini
There are 5 configuration fields:
* SourceDirectory
  - It must not be empty.
  - Enter there the full path of directory from which files should be moved.
  - If there is a backslash '\' in the path, you need to enter two backslashes instead of one. (For the network paths it should start like this: \\\\192.168.0.1\\directory)
* DestinationDirectory
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
SourceDirectory=C:\\source\\directory
DestinationDirectory=C:\\some\\other\\directory
Extensions=*
; or Extensions=
LengthComparator=greaterequal
FileLength=0
```

### Multiple Tasks
When you want to create multiple tasks, you can do it by inserting new children in the config file with UNIQUE name
All of the others field must have the same names as described above.
