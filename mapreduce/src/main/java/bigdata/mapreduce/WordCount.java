package bigdata.mapreduce;
// 리눅스 2024-07-23 수업

// Hadoop 시스템 구축

//hadoop 3.3.1 -> MapReduce -> Tutorial -> Example: WordCount v1.0 -> Source Code 복사
// => ctrl + shift + o 해서 import 안쓰는 거(노란색줄) 지워주기
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}

// 프로젝트의 클래스들을 다 만들고 pom 다 추가하고 오류까지 싹 사라졌으면 
// 프로젝트에서 우클릭 후 Maven -> Update 해주기
// 프로젝트에서 우클릭 후 Run as -> Maven build -> Run 해주기 -> Console에 초록색 글씨로 BUILD SUCCESS 뜨면 완료

// target의 maven-status의 mapreduce-0.0.1을 바탕화면에 빼주기
// => 오류나면 maven-status안에 있는 파일들 다 없앴다가 다시 만들어주면 됨(자동으로 만들어짐)