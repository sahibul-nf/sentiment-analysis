//package sentiment;
//
//import org.apache.storm.tuple.Values;
//import sentiment.bolts.FilteringBolt;
//import sentiment.bolts.PositiveBolt;
//import sentiment.entitys.UserReview;
//import sentiment.utils.Cons;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//
///** Hello world! */
//public class Topology {
//    public static void main(String[] args) {
//
//        List<UserReview> data = new ArrayList<UserReview>();
//        // SPOUT
//        try {
//            String line;
//            String fileName = "src/main/resources/reviews.csv";
//            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
//
//            while ((line = fileReader.readLine()) != null) {
//                String[] reviews = line.split(",");
//
//                UserReview userReview = new UserReview();
//                userReview.setItemID(reviews[0]);
//                userReview.setCategory(reviews[1]);
//                userReview.setReviewTitle(reviews[5]);
//                userReview.setReviewContent(reviews[6]);
//
//                data.add(userReview);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("File tidak ditemukan " + e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // FILTER
//        try {
//            String text = tuple.getStringByField(Cons.TUPLE_VAR_MSG);
//
//            String[] words = text.split("\\b");
//            StringBuilder modifiedText = new StringBuilder();
//
//            for (String word : words) {
//                if (!FilteringBolt.UselessWords.get().contains(word)) {
//                    modifiedText.append(word);
//                }
//            }
//
//            basicOutputCollector.emit(new Values(modifiedText));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // POSITIVE
//        int wordsSize = words.length;
//        int positiveWordsSize = 0;
//
//        for (String word : words) {
//            if (PositiveBolt.PositiveWords.get().contains(word)) {
//                positiveWordsSize++;
//            }
//        }
//
//        double positiveResult = (double) positiveWordsSize / wordsSize;
//
//        UserReview userReview = new UserReview();
//        userReview.setPositiveScore(positiveResult);
//    }
//
//    // USELESSWORD
//    private static class UselessWords {
//        private Set<String> uselessWords;
//        private static UselessWords _singleton;
//
//        private UselessWords() {
//            try {
//                String fileName = "src/main/resources/useless-words.txt";
//                BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
//                String line;
//                uselessWords = new HashSet<>();
//
//                while ((line = fileReader.readLine()) != null) {
//                    uselessWords.add(line);
//                }
//            } catch (FileNotFoundException e) {
//                System.out.println("File tidak ditemukan " + e);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        static UselessWords get() {
//            if (_singleton == null) {
//                synchronized (UselessWords.class) {
//                    if (_singleton == null) {
//                        _singleton = new UselessWords();
//                    }
//                }
//            }
//
//            return _singleton;
//        }
//
//        boolean contains(String key) {
//            return get().uselessWords.contains(key);
//        }
//    }
//}
