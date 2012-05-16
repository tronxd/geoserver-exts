package org.opengeo.data.csv;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengeo.data.csv.parse.CSVStrategy;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

import com.csvreader.CsvReader;

public class CSVDataStore extends ContentDataStore implements FileDataStore {

    private final CsvReader csvReader;

    private final CSVStrategy csvStrategy;

    private final String typeName;

    private static String typeNameFromFile(File file) {
        String path = file.getPath();
        String baseName = FilenameUtils.getBaseName(path);
        return baseName;
    }

    public CSVDataStore(File file) throws IOException {
        this(file, typeNameFromFile(file));
    }

    public CSVDataStore(File file, String typeName) throws IOException {
        this(file, typeName, new LatLonStrategyFactory(typeName, DefaultGeographicCRS.WGS84));
    }

    public CSVDataStore(File file, String typeName, CSVStrategyFactory csvStrategyFactory)
            throws IOException {
        this.typeName = typeName;
        this.csvReader = new CsvReader(file.getPath());

        boolean readHeaders = this.csvReader.readHeaders();
        if (!readHeaders) {
            throw new IOException("Failure reading csv header for: " + file.getPath());
        }

        this.csvStrategy = csvStrategyFactory.createCSVStrategy(csvReader.getHeaders());
    }

    public Name getTypeName() {
        return new NameImpl(typeName);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return Collections.singletonList(getTypeName());
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new CSVFeatureSource(entry, Query.ALL);
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return this.csvStrategy.getFeatureType();
    }

    @Override
    public void updateSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleFeatureSource getFeatureSource() throws IOException {
        return new CSVFeatureSource(this);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException {
        return new CSVFeatureSource(this).getReader();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Filter filter,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    public CsvReader getCsvReader() {
        return csvReader;
    }

    public CSVStrategy getCSVStrategy() {
        return csvStrategy;
    }

}
