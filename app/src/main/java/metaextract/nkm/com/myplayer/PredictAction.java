package metaextract.nkm.com.myplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import static metaextract.nkm.com.myplayer.FileManager.getPublicPicturesDirectory;

public class PredictAction {
    private String[] sensors, functions;

    public PredictAction(String[] sensors, String[] functions) {
        this.sensors = sensors;
        this.functions = functions;
    }

    public String getPrediction(String songName, String startTime, String finishTime) {
        double predictValue;
        String predictString = "";
        ArrayList<Attribute> attributes = getAttributes();
        File inputModel = getPublicPicturesDirectory("Knn.model");
        ArithmeticFunctions fun = new ArithmeticFunctions();
        MyLambdaFunction[] LambdaFunctions = {fun.getAvg(), fun.getSD()};
        DataVector dv = new DataVector();
        dv.GetDataFromSensors(songName, startTime, finishTime);
        double[] att = dv.getFinalVector(LambdaFunctions);
        double[] attributeValuesArr = new double[att.length + 1];
        System.arraycopy(att, 0, attributeValuesArr, 0, att.length);
        attributeValuesArr[attributeValuesArr.length - 1] = 0; //set the class value to be 0 (missedValue)
        Instances dataSet = new Instances("SingleInstance", attributes, 1);
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        DenseInstance instance = new DenseInstance(1, attributeValuesArr);//fill this instance with values from sensors
        instance.setDataset(dataSet);
        try {//load the trained model and make a prediction for a single instance.
            IBk ibk = (IBk) weka.core.SerializationHelper.read(inputModel.getPath());
            predictValue = ibk.classifyInstance(instance);
            predictString = dataSet.classAttribute().value((int) predictValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return predictString;
    }

    private Instances getInstanceFromCSV(String filename) {
        CSVLoader csv = new CSVLoader();
        try {
            csv.setSource(new File(filename));
            csv.setMissingValue("null");
            return csv.getDataSet();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Attribute> getAttributes() {
        FileManager fm = new FileManager();
        ArrayList<Attribute> res = new ArrayList<>();
        String[] allAttributes = fm.addFunctionsToAttributesNames("notDataVector", sensors, functions);
        for (String att :
                allAttributes) {
            res.add(new Attribute(att));
        }
        res.add(new Attribute("@@Action@@", getClasses()));
        return res;
    }

    private ArrayList<String> getClasses() {
        ArrayList<String> res = new ArrayList<>();
        res.add("Progress bar");
        res.add("next");
        res.add("forward");
        res.add("Play");
        res.add("Shuffle on");
        res.add("Stop");
        res.add("Choose song");
        res.add("Shuffle off");
        res.add("do nothing");
        return res;
    }
}
