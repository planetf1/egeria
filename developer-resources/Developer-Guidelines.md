<!-- SPDX-License-Identifier: Apache-2.0 -->

# Developer Guidelines

Egeria provides technology for an open standard that seeks to improve the processing and
protection of data across organizations.  For its developers, this carries the benefit that
their work receives high recognition, but also additional responsibilities to ensure its 
wide applicability and longevity.   

For example, Egeria seeks a broad audience - from developers to adopting vendors to consuming users.
Building this audience and allowing the community to scale requires clarity in the way
the software is written, documented, packaged and used.  Many of the guidelines seek to make
it easier for someone new to pick up the software, at the expense of maybe a little more work,
or a little less freedom of action for the original developer.

As such, these guidelines exist to remind us of these broader responsibilities.

## License text in files

All files for Egeria should have a license included.  We are using the Apache 2.0 license, 
which protects our code whilst still allowing commercial exploitation of the code.  There is
an example of the license text at the top of this file.  The following files in the
**License-Example-Files** directory have the correct
license information formatted for different file types to make it easy to use.

* [License for Markdown Files.md](./License-Example-Files/License_for_Markdown_Files.md)
* [License for POM Files.xml](./License-Example-Files/License_for_POM_Files.xml)
* [License for Java Files.java](./License-Example-Files/License_for_Java_Files.java)

Notice that the license information is coded using [SPDX](https://spdx.org/ids).

## Documentation

Although all code for Egeria should be clear and easy to read, the code itself can only
describe **what** it is doing.  It can rarely describe **why** it is doing it.  Also, the
Egeria codebase is quite large and hard to digest in one go.  Having summaries of its
behaviour and philosophy helps people to understand its capability faster.

### README markdown files

Each directory (particularly code modules) should have a `README.md` file that describes the
content of the directory.  These files are displayed automatically by GitHub when the
directory is accessed and this helps someone navigating through the directory structures.

The exception is that directories representing Java packages do not need README files
because they are covered by Javadoc.

### Javadoc

[Javadoc](https://docs.oracle.com/javase/7/docs/technotes/tools/solaris/javadoc.html)
is used to build a code reference for our public site.  It is generated
as part of the build.  There are three places where Javadoc should be provided
by the developer of Java code:

* Every Java source file should begin with a header javadoc tag just before the start of the
class/interface/enum, which explains the purpose and responsibilities of the code.
* All public methods should have a clear Javadoc header describing the purpose, parameters and
results (including exceptions).  This includes test cases.
* Each Java package should include a `package-info.java` file describing the purpose of the
package and its content.

Java code files may have additional comments, particularly where the processing is complex.
The most useful comments are those that describe the purpose, or intent of the code,
rather than a description of what each line of code is doing.

## Dependent libraries

New dependencies **must** only be introduced with the agreement of the broader
community.  These include frameworks, utility classes, annotations and external packages.
This may seem annoying but there are good reasons for this:

* The Egeria code needs to be embeddable in many different vendor products.
This is made easier by keeping the code libraries we are dependent on to the minimum
in order to avoid conflicts with libraries a consuming vendor may have already chosen.
* As developers, we have legal obligations to ensure we only use appropriately
licensed software in our work and part of the discussion related a new dependency
is to understand its license.
* Some projects may provide useful functionality but are only supported by one
person who may get bored with it, or no longer have the time to support it.
We should aim to build on dependent libraries that are backed by a strong
community or vendor.
* Each library function, or set of annotations, adds to the learning curve of
new people joining the team.  By only bringing in the really beneficial
libraries we ensure that the complexity they see relates only to the complexity
of the problem space, rather than the additional complexity we have introduced in
pursuit of playing with new functions.

If a developer wishes to introduce a new dependency to the Egeria project,
they should prepare a short guide (in a markdown file) that explains the value of
the new library, how it is to be used and links to more information.
They should then present their recommendation to the community and
and if agreed by the community, store the guide in the developer resources.

Once in place, the dependency should be maintained across the smallest
number of modules - particularly when it may impact consuming technologies.

For example:
* Jackson is used to convert between Java POJOs and JSON.  It is used throughout the
open metadata code base and so its dependency is located in the top level pom file.
* Spring provides annotations for REST APIs.  Again it is used in many of the modules.
However, care is made to push this dependency to small leaf-node modules since
the Spring libraries may interfere with other RESTful frameworks.

Optimising the dependency structure is something that will evolve as we gain more
experience with other embedding technologies.

## Coding style and layout

There are many coding and layout styles that provide clear and readable code.
Developers can choose the layout they prefer but with the following
restrictions/suggestions:

* Try to use full words rather than abbreviations or shortened versions of
a word for names such as class names, method names and variable names.
Cryptic names create more effort for the reader to follow the code.
* Use the same style throughout a file.
* If changing an existing file, use the same style and layout as the original
developer.  Don't impose your own style in the middle of the code since the
inconsistency that you introduce makes the whole file harder to read.
It should not be possible to see where you have made the changes once the
code is committed into git.

## Testing

All code submissions should be accompanied by automated tests that validate
the essential behaviour of the code.  These automated tests should be
incorporated in the build so that they run either at the **test** or **verify**
stages of the build.

Our preferred Java test frameworks are [TestNG](http://testng.org) and [Mockito](http://mockito.org).