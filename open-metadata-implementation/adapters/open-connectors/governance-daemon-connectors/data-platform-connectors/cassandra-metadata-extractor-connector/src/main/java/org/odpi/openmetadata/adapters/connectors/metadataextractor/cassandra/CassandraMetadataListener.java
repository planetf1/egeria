package org.odpi.openmetadata.adapters.connectors.metadataextractor.cassandra;

import com.datastax.oss.driver.api.core.metadata.schema.*;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformDeployedDatabaseSchema;

import java.io.Closeable;

public class CassandraMetadataListener implements SchemaChangeListener,Runnable {

    DataPlatformDeployedDatabaseSchema dataPlatformDeployedDatabaseSchema;

    @Override
    public void onKeyspaceCreated(@NonNull KeyspaceMetadata keyspaceMetadata) {


    }

    @Override
    public void onKeyspaceDropped(@NonNull KeyspaceMetadata keyspaceMetadata) {

    }

    @Override
    public void onKeyspaceUpdated(@NonNull KeyspaceMetadata keyspaceMetadata, @NonNull KeyspaceMetadata keyspaceMetadata1) {

    }

    @Override
    public void onTableCreated(@NonNull TableMetadata tableMetadata) {

    }

    @Override
    public void onTableDropped(@NonNull TableMetadata tableMetadata) {

    }

    @Override
    public void onTableUpdated(@NonNull TableMetadata tableMetadata, @NonNull TableMetadata tableMetadata1) {

    }

    @Override
    public void onUserDefinedTypeCreated(@NonNull UserDefinedType userDefinedType) {

    }

    @Override
    public void onUserDefinedTypeDropped(@NonNull UserDefinedType userDefinedType) {

    }

    @Override
    public void onUserDefinedTypeUpdated(@NonNull UserDefinedType userDefinedType, @NonNull UserDefinedType userDefinedType1) {

    }

    @Override
    public void onFunctionCreated(@NonNull FunctionMetadata functionMetadata) {

    }

    @Override
    public void onFunctionDropped(@NonNull FunctionMetadata functionMetadata) {

    }

    @Override
    public void onFunctionUpdated(@NonNull FunctionMetadata functionMetadata, @NonNull FunctionMetadata functionMetadata1) {

    }

    @Override
    public void onAggregateCreated(@NonNull AggregateMetadata aggregateMetadata) {

    }

    @Override
    public void onAggregateDropped(@NonNull AggregateMetadata aggregateMetadata) {

    }

    @Override
    public void onAggregateUpdated(@NonNull AggregateMetadata aggregateMetadata, @NonNull AggregateMetadata aggregateMetadata1) {

    }

    @Override
    public void onViewCreated(@NonNull ViewMetadata viewMetadata) {

    }

    @Override
    public void onViewDropped(@NonNull ViewMetadata viewMetadata) {

    }

    @Override
    public void onViewUpdated(@NonNull ViewMetadata viewMetadata, @NonNull ViewMetadata viewMetadata1) {

    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * <p>While this interface method is declared to throw {@code
     * Exception}, implementers are <em>strongly</em> encouraged to
     * declare concrete implementations of the {@code close} method to
     * throw more specific exceptions, or to throw no exception at all
     * if the close operation cannot fail.
     *
     * <p> Cases where the close operation may fail require careful
     * attention by implementers. It is strongly advised to relinquish
     * the underlying resources and to internally <em>mark</em> the
     * resource as closed, prior to throwing the exception. The {@code
     * close} method is unlikely to be invoked more than once and so
     * this ensures that the resources are released in a timely manner.
     * Furthermore it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     *
     * <p><em>Implementers of this interface are also strongly advised
     * to not have the {@code close} method throw {@link
     * InterruptedException}.</em>
     * <p>
     * This exception interacts with a thread's interrupted status,
     * and runtime misbehavior is likely to occur if an {@code
     * InterruptedException} is {@linkplain Throwable#addSuppressed
     * suppressed}.
     * <p>
     * More generally, if it would cause problems for an
     * exception to be suppressed, the {@code AutoCloseable.close}
     * method should not throw it.
     *
     * <p>Note that unlike the {@link Closeable#close close}
     * method of {@link Closeable}, this {@code close} method
     * is <em>not</em> required to be idempotent.  In other words,
     * calling this {@code close} method more than once may have some
     * visible side effect, unlike {@code Closeable.close} which is
     * required to have no effect if called more than once.
     * <p>
     * However, implementers of this interface are strongly encouraged
     * to make their {@code close} methods idempotent.
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }
}