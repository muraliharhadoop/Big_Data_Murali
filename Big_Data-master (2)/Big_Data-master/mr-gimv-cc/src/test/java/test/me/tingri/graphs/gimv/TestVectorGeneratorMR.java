package test.me.tingri.graphs.gimv;


import me.tingri.graphs.gimv.VectorGeneratorMapper;
import me.tingri.graphs.gimv.VectorGeneratorReducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.MapDriver;
import org.apache.hadoop.mrunit.MapReduceDriver;
import org.apache.hadoop.mrunit.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static me.tingri.util.CONSTANTS.*;
import static test.me.tingri.graphs.gimv.TESTDATA.*;

public class TestVectorGeneratorMR {
    MapDriver<LongWritable, Text, LongWritable,Text> mapDriver;
    ReduceDriver<LongWritable, Text,LongWritable,Text> reduceDriver;
    MapReduceDriver<LongWritable, Text, LongWritable, Text, LongWritable, Text> mapReduceDriver;

    @Before
    public void setUp() {
        VectorGeneratorMapper mapper = new VectorGeneratorMapper();
        VectorGeneratorReducer reducer = new VectorGeneratorReducer();

        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);

        mapDriver.getConfiguration().set(MAKE_SYMMETRIC,"1");
        mapDriver.getConfiguration().set(FIELD_SEPARATOR, DEFAULT_FIELD_SEPARATOR);
        mapDriver.getConfiguration().set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);


        reduceDriver.getConfiguration().set(MAKE_SYMMETRIC,"1");
        reduceDriver.getConfiguration().set(FIELD_SEPARATOR, DEFAULT_FIELD_SEPARATOR);
        reduceDriver.getConfiguration().set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);

        mapReduceDriver.getConfiguration().set(MAKE_SYMMETRIC,"1");
        mapReduceDriver.getConfiguration().set(FIELD_SEPARATOR, DEFAULT_FIELD_SEPARATOR);
        mapReduceDriver.getConfiguration().set(VECTOR_INDICATOR, DEFAULT_VECTOR_INDICATOR);
    }

    @Test
    public void testMapper() throws IOException {
        mapDriver.withAll(getVectorGeneratorMapperInput());

        mapDriver.withAllOutput(getVectorGeneratorMapperOutput());

        //though code is deterministic it is not necessary to function. Hence accepting results in any order.
        mapDriver.runTest(false);
    }

    @Test
    public void testReducer() throws IOException {

        reduceDriver.withAll(getVectorGeneratorReducerInput());

        reduceDriver.withAllOutput(getVectorGeneratorReducerOutput());

        //since HashSet is used, results are non-deterministic. Hence order does not matter
        reduceDriver.runTest(false);
    }


    @Test
    public void testMapReduce() throws IOException {
        mapReduceDriver.withAll(getVectorGeneratorMapperInput());

        mapReduceDriver.withAllOutput(getVectorGeneratorReducerOutput());

        //since HashSet is used in reducer, results are non-deterministic. Hence order does not matter
        mapReduceDriver.runTest(false);
    }

}
