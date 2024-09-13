package bigdata.mapreduce;

// WordCount에 있는 import 싹 다 복사 붙여넣기 => ctrl + shift + o 해서 import 안쓰는 거(노란색줄) 지워주기
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

// WordCount에서 만든 ToknizerMapper 가져와서 static 지우기
public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
		}
	}
}
