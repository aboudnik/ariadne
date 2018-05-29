package org.boudnik.ariadne;

import org.boudnik.ariadne.handlers.HandlerFactory;
import org.mvel2.templates.TemplateRuntime;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Alexandre_Boudnik
 * @since 05/24/2018
 */
public abstract class External<R> extends DataBlock<R> {
    public External(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public String build(DataFactory factory) throws IOException, IllegalAccessException, NoSuchMethodException {
        DataFactory.LOGGER.fine("LOAD  " + key());
        DataSource dataSource = factory.getDataSource(type());
        assert dataSource != null;
        String url = (String) TemplateRuntime.eval(dataSource.getTemplate(), dimensions());
        Collection collection = HandlerFactory.getInstance().getHandler(url).handle(this);
        DataFactory.LOGGER.fine("loaded from "+ url);
        DataFactory.LOGGER.fine("loaded data "+ collection);
        DataFactory.LOGGER.fine("loaded data "+ collection.stream().filter(lambda()).collect(Collectors.toList()));
        return url;
    }
}
