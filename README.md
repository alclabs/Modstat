Purpose
=======

The Modstat Add-On to WebCTRL gathers module status reports from all the controllers beneath
the selected location in the tree and parses them. It provides checks for a range of problems
including:

* No Communications
* Watchdog Timeouts
* Error Messages
* Program not running
* Low free database, file storage, or memory space
* BACnet comm errors over the last 7 days
* Excessive ARCnet reconfigs
* Ethernet transmit / receive errrors
* No Driver
* No Programs

Currently, the ModuleStatus Aspect in the directaccess add-on api only gets the modstat as a
string. This add-on is the first to actually parse the modstat into more useful components. We
would like to have this parsing incorporated into the add-on api at some point.

Project Status
--------------
While there are more features I would like to add, this seems functional now.  It should be useful
on a real production system.

Outstanding issues:

* Allow canceling while gathering downloads
* Would like to save counts and check for changes

Support and Other Downloads
---------------------------
The [ALCshare.com](http://www.alcshare.com) site should be used for discussion or support for
this add-on. Binaries for this and other add-ons are available there.