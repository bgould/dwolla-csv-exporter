CSV exporter for Dwolla API
===========================

This is a Java application/library for exporting data from [Dwolla][1].

It is not advanced or special.  At the moment it does not rely on any
external libraries or any features beyond Java 5 (although I've not
tried to compile using a Java 5 environment).

Right now you can only export a complete list of transactions.  I will
add more operations in the future if there is a need.  You can always
[file an issue][2] if you have a request for enhanced functionality.

License
-------

The MIT License (MIT)

Copyright (c) 2014 Benjamin Gould

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

Some portions (the org.json.* package specifically) copyright 2011 by
[Douglas Crockford][json] and carry the restriction "The software shall be 
used for good, not evil."  Please comply with this restriction.


Building from source
--------------------

You will need a Java development environment installed, and Maven 2.

> $ mvn install

Look for the JAR file in the `target` directory after you see the 
`BUILD SUCCESSFUL` message.

Running the application
-----------------------

A command line interface is provided.  After building you can run it like this:

> $ java -jar target/dwolla-csv-exporter-1.0.jar -o 'your_oauth_token'

That will export your transactions to `STDOUT`.  To save the output to a file:

> $ java -jar target/dwolla-csv-exporter-1.0.jar -o 'your_oauth_token' > output.csv

You can get an OAuth token, you can easily [generate one here][3].  Guard 
this token because it non-expiring access to your account.  It might be 
a good idea use your Dwolla account settings to remove permissions for 
the Developer's Portal after you're done with the token, for good measure.
You have been warned.

Expressing your Gratitude
-------------------------

If you like this utility, no Thank You is necessary.  Cash is readily accepted 
though.  [Click here to send me some.][4]

[1]: http://refer.dwolla.com/a/clk/pwxsm
[2]: https://github.com/bgould/dwolla-csv-exporter/issues
[3]: https://developers.dwolla.com/dev/token
[4]: https://forms.dwollalabs.com/send-me-some-cash
[json]: http://json.org/
