package de.ctoffer.damal.processor;

import de.ctoffer.commons.io.StdIo;
import de.ctoffer.damal.antlr4.DaMaLLexer;
import de.ctoffer.damal.antlr4.DaMaLParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ParserDemo {
    public static void main(String[] args) throws IOException {

        CharStream input = CharStreams.fromString("3 + 4*2-5");
        DaMaLLexer lexer = new DaMaLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DaMaLParser parser = new DaMaLParser(tokens);
        DaMaLParser.OperationContext tree = parser.operation();

        StdIo.print(Trees.toStringTree(tree, Arrays.asList(DaMaLParser.ruleNames)));

        ParseTreeListener extractor = new MyListener(parser);
        ParseTreeWalker.DEFAULT.walk(extractor, tree);
    }

    private static class MyListener implements ParseTreeListener {
        private DaMaLParser parser;

        public MyListener(DaMaLParser parser) {
            this.parser = parser;
        }

        @Override
        public void visitTerminal(TerminalNode terminalNode) {
            StdIo.print("visitTerminal", terminalNode);
        }

        @Override
        public void visitErrorNode(ErrorNode errorNode) {
            StdIo.print("visitErrorNode", errorNode);
        }

        @Override
        public void enterEveryRule(ParserRuleContext parserRuleContext) {
            StdIo.print("enterEveryRule", parserRuleContext);
        }

        @Override
        public void exitEveryRule(ParserRuleContext parserRuleContext) {
            StdIo.print("exitEveryRule", parserRuleContext);
        }
    }
}
