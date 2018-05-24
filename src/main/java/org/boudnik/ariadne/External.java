package org.boudnik.ariadne;

import org.mvel2.templates.TemplateRuntime;

/**
 * @author Alexandre_Boudnik
 * @since 05/24/2018
 */
public abstract class External<R> extends DataBlock<R> {
    public External(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public String build(DataFactory factory) {
        DataFactory.LOGGER.fine("LOAD  " + key());
        DataSource dataSource = factory.getDataSource(type());
        assert dataSource != null;
        String url = (String) TemplateRuntime.eval(dataSource.getTemplate(), dimensions());
        DataFactory.LOGGER.fine("loaded from "+ url);
        return url;
    }
}
