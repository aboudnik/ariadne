package org.boudnik.ariadne;

import org.apache.spark.sql.Dataset;

import java.io.Serializable;

/**
 * @author Alexandre_Boudnik
 * @author Sergey Nuyanzin
 * @since 05/24/2018
 */
public abstract class External<R extends Serializable> extends DataBlock<R> {
    public External(Dimension... dimensions) {
        super(dimensions);
    }

    @Override
    public final Dataset<R> build(DataFactory factory) {
        DataFactory.LOGGER.info("LOAD  " + key());
        DataSource<R> dataSource = factory.getDataSource(type());
        return save(dataSource, load(dataSource, factory.getSession()));
    }

    @Override
    public String sql() {
        return null;
    }
}
