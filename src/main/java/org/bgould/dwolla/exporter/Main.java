package org.bgould.dwolla.exporter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main (String... args) {
        Options options = new Options();
        options.addAllowedOption(new OAuthOption());
        try {
            Map<String, OptionValuePair> optionValues = options.parse(args);
            if (!optionValues.containsKey(OAuthOption.NAME)) {
                throw new IllegalArgumentException("OAuth token must be provided.");
            }
            final TransactionListExporter exporter = new TransactionListExporter();
            exporter.setOauthToken(optionValues.get(OAuthOption.NAME).values()[0]);
            exporter.export();
        } catch (IllegalArgumentException e) {
            System.err.println();
            System.err.println(e.getMessage());
            System.err.println();
            System.err.println(options.helpText());
            System.err.println();
            System.exit(1);
        }
    }

    public static class Options {

        private final List<Option> allowedOptions = new ArrayList<Option>();

        private enum NextToken {
            OPTION,
            ARGUMENT
        }
        
        public void addAllowedOption(Option _option) {
            this.allowedOptions.add(_option);
        }

        public Option[] getAllowedOptions() {
            return allowedOptions.toArray(new Option[allowedOptions.size()]);
        }

        public Map<String, OptionValuePair> parse(String[] args) {
            final Map<String, OptionValuePair> result = new LinkedHashMap<String, OptionValuePair>();
            NextToken nextToken = NextToken.OPTION;
            Option option = null;
            List<String> arguments = null;
            for (String arg : args) {
                if (nextToken.equals(NextToken.OPTION)) {
                    for (Option _option : allowedOptions) {
                        if (    (arg.equals("--" + _option.name())) || 
                                (_option.abbr() != null && arg.equals("-" + _option.abbr()) )
                            ) {
                            if (result.containsKey(_option.name())) {
                                throw new IllegalArgumentException(
                                    String.format("Option `%s` can only be specified once.", _option.name())
                                );
                            }
                            option = _option;
                            arguments = new ArrayList<String>(option.numberOfArguments());
                            nextToken = NextToken.ARGUMENT;
                        }
                    }
                    if (option == null) {
                        throw new IllegalArgumentException("Unrecognized option: " + arg);
                    }
                } else if (nextToken.equals(NextToken.ARGUMENT)) {
                    arguments.add(arg);
                } else {
                    throw new IllegalStateException("nextToken must either be an option or an argument");
                }
                if (nextToken.equals(NextToken.ARGUMENT) && arguments.size() == option.numberOfArguments()) {
                    nextToken = NextToken.OPTION;
                }
                if (nextToken.equals(NextToken.OPTION)) {
                    String[] argumentArray = arguments.toArray(new String[arguments.size()]);
                    option.validate(argumentArray);
                    result.put(option.name(), new OptionValuePair(option, argumentArray));
                    option = null;
                    arguments = null;
                }
            }
            if (nextToken.equals(NextToken.ARGUMENT)) {
                String[] argumentArray = arguments.toArray(new String[arguments.size()]);
                option.validate(argumentArray);
                result.put(option.name(), new OptionValuePair(option, argumentArray));
                option = null;
                arguments = null;
            }
            return result;
        }

        public String helpText() {
            StringBuilder buff = new StringBuilder();
            buff.append("Available options include:\n")
                .append("--------------------------\n\n");
            for (Option option : getAllowedOptions()) {
                buff.append("    ").append(option.helpText()).append("\n");
            }
            buff.append("\n");
            return buff.toString();
        }
    }

    public static class OptionValuePair {
        
        private final Option option;
        
        private final String[] values;
        
        public OptionValuePair(Option _option, String[] _values) {
            this.option = _option;
            this.values = _values;
        }
        
        public Option option() {
            return this.option;
        }
        
        public String[] values() {
            return this.values;
        }
        
    }

    public static class Option {
        
        private final String name;
        
        private final String abbr;
        
        private final String[] arguments;
        
        private final String description;
        
        public Option (String name, String abbr, String description, String... arguments) {
            this.name = name;
            this.abbr = abbr;
            this.description = description;
            this.arguments = arguments;
        }
        
        public String name() {
            return name;
        }
        
        public String abbr() {
            return abbr;
        }
        
        public int numberOfArguments() {
            return arguments.length;
        }
        
        public String argument(int index) {
            return arguments[index];
        }
        
        public String[] arguments() {
            final String[] copy = new String[arguments.length];
            System.arraycopy(arguments, 0, copy, 0, arguments.length);
            return copy;
        }
        
        public String description() {
            return description;
        }
        
        public String helpText() {
            StringBuilder buff = new StringBuilder();
            buff.append("--").append(name());
            if (this.abbr() != null) {
                buff.append(",-").append(abbr());
            }
            if (numberOfArguments() > 0) {
                buff.append(" <");
                for (int i = 0, c = numberOfArguments(); i < c; i++) {
                    String argumentName = argument(i);
                    buff.append(argumentName);
                    if ((i + 1) < c) {
                        buff.append(", ");
                    }
                }
                buff.append(">");
            }
            buff.append(" - ").append(description());
            return buff.toString();
        }
        
        public void validate(String... _arguments) throws IllegalArgumentException {
            final String[] arguments = arguments(); 
            if (_arguments.length != arguments.length) {
                throw new IllegalArgumentException(
                    String.format(
                        "Expected %s arguments, %s provided", 
                        arguments.length, _arguments.length
                    )
                );
            }
            for (int i = 0, c = arguments.length; i < c; i++) {
                if (_arguments[i] == null || _arguments[i].trim().equals("")) {
                    throw new IllegalArgumentException(
                        String.format("Argument `%s` cannot be blank.", arguments[i]));
                }
            }
        }
        
    }
    
    public static class OAuthOption extends Option {
        
        public static final String NAME = "oauth";
        
        public OAuthOption() {
            super(NAME, "o", "Supply a Dwolla OAuth token to use for extracting the transactions.", "token");
        }
        
    }
    
}
