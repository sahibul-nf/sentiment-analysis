package sentiment;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import sentiment.bolts.FilteringBolt;
import sentiment.bolts.NegativeBolt;
import sentiment.bolts.PositiveBolt;
import sentiment.bolts.ScoringBolt;
import sentiment.spouts.UserReviewSentenceSpout;

public class SentimentTopology {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        final TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new UserReviewSentenceSpout());

        builder.setBolt("filtering", new FilteringBolt(), 1).shuffleGrouping("spout");
        builder.setBolt("positive", new PositiveBolt(), 1).shuffleGrouping("filtering");
        builder.setBolt("negative", new NegativeBolt(), 1).shuffleGrouping("positive");
        builder.setBolt("scoring", new ScoringBolt(), 1).shuffleGrouping("negative");

        Config config = new Config();
        config.setDebug(true);

        // membuat instansiasi class LocalCluster
        // untuk menjalankan topology di mode local
        LocalCluster cluster = new LocalCluster();
        // submit LearnStormTopology ke topology local
        cluster.submitTopology("LearnStormTopology", config, builder.createTopology());
        //        StormSubmitter.submitTopology("LearnStormTopology", config,
        // builder.createTopology());

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            System.out.println("Ada Kesalahan ni : " + e);
        }

        // kill LearnStormTopology
        cluster.killTopology("LearnStormTopology");
        // dan shutdown storm cluster
        cluster.shutdown();

        //        if (args != null && args.length > 0) {
        //            config.setNumWorkers(1);
        //
        //            try {
        //                StormSubmitter.submitTopologyWithProgressBar(args[0], config,
        // builder.createTopology());
        //            } catch (Exception e) {
        //                System.out.println("Ada error ni : " +e);
        //            }
        //
        //        } else {
        //            try {
        //                LocalCluster localCluster = new LocalCluster();
        //                localCluster.submitTopology("SentimentTopology", config,
        // builder.createTopology());
        //
        //                Thread.sleep(10000);
        //
        //                localCluster.killTopology("SentimentTopology");
        //                localCluster.shutdown();
        //            } catch (Exception e) {
        //                System.out.println("Ada error ni : " +e);
        //            }
        //        }
    }
}
