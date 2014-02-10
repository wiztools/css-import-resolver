# CSS Import Resolver Cli

A tool which parses the CSS @import url() statement and adds the content of the file inline to the CSS. The output is by default written to the STDOUT, or using the -o option written to a file.

## Details

In CSS you can refer to other CSS files thus:

    @import url(path/to/included.css);

Such reference makes the code easy to read and maintain, but on the performance side it takes a hit---because the browser needs to make multiple HTTP requests to render this page ([more detail](http://www.stevesouders.com/blog/2009/04/09/dont-use-import/)).

This tool reads such CSS files and includes the content of the @import'ed file in the main CSS.

This can be done as a build time processing for building faster sites.

## Usage

To list the command-line options:

    $ java -jar css-import-resolver-NN-jar-with-dependencies.jar -h

### Limitations

1. The parsing is done using RegularExpression, so when @import statements are encountered inside CSS comments, they are also expanded.
2. HTTP urls are not processed.

