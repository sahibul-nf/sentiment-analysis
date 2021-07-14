package sentiment.spouts;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import sentiment.entitys.UserReview;
import sentiment.utils.Cons;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserReviewSentenceSpout extends BaseRichSpout {

    private static final long serialVersionUID = 1L;
    //    private ClassLoader classLoader = getClass().getClassLoader();
    String line;
    private SpoutOutputCollector collector;
    private List<UserReview> data = new ArrayList<UserReview>();
    boolean isCompleted;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;

        try {
            String fileName = "src/main/resources/reviews.csv";
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));

            while ((line = fileReader.readLine()) != null) {
                String[] reviews = line.split(",");

                UserReview userReview = new UserReview();
                userReview.setItemID(reviews[0]);
                userReview.setCategory(reviews[1]);
                userReview.setReviewTitle(reviews[5]);
                userReview.setReviewContent(reviews[6]);

                data.add(userReview);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextTuple() {
        if (!isCompleted) {
            for (UserReview content : data) {
                collector.emit(new Values(content.getReviewContent()));
            }
            isCompleted = true;
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(Cons.TUPLE_VAR_MSG));
    }

}
