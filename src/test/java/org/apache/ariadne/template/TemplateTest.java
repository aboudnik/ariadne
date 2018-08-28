package org.apache.ariadne.template;

import org.junit.Test;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alexandre_Boudnik
 * @since 08/27/2018
 */
public class TemplateTest {

    private final List<Instrument> instruments = new ArrayList<Instrument>() {{
        add(new Instrument("US11CM1LB"));
        add(new Instrument("US10CM3AA"));
        add(new Instrument("US11CM3AA"));
        add(new Instrument("US21CM3AA"));
        add(new Instrument("US31CM1AA"));
        add(new Instrument("US40CM1AA"));
        add(new Instrument("US22CM3AA"));
        add(new Instrument("US67CM1AA"));
        add(new Instrument("US73CM3AA"));
        add(new Instrument("US74CM1AA"));
    }};


    public static class Instrument {
        public String symbol;

        Instrument(String symbol) {
            this.symbol = symbol;
        }
    }

    @Test
    public void main() {
        CompiledTemplate template = TemplateCompiler.compileTemplate(TemplateTest.class.getResourceAsStream("/template.txt"));
        String execute = (String) TemplateRuntime.execute(template, new HashMap<String, Object>() {{
            put("instruments", instruments);
        }});
        System.out.println(execute);
    }
}
