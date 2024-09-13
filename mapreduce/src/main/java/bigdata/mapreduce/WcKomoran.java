package bigdata.mapreduce;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class WcKomoran {
	
	public static void main(String[] args) { 
        
        String strDirPath = "C:\\Users\\ggg\\Desktop\\Virtual_Hadoop\\04.WordCount\\대통령신년사"; 
         
        ListFile( strDirPath ); 
    } 
     
    // 재귀함수 
    private static void ListFile( String strDirPath ) { 
         
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        
        File path = new File( strDirPath ); 
        File[] fList = path.listFiles(); 

        String content = null;
        for( int i = 0; i < fList.length; i++ ) { 
             
            // 문서파일 내용 읽어오기  
            if( fList[i].isFile() ) { 
            	content = null;
            	try {
         			content = Files.lines(Paths.get(fList[i].getPath()), StandardCharsets.UTF_8).
        					collect(Collectors.joining(System.lineSeparator()));
            	} catch (IOException e) {
        			e.printStackTrace();
        		}
            } 
            else if( fList[i].isDirectory() ) { 
                ListFile( fList[i].getPath() );  // 재귀함수 호출 
            } 
            
            // 문서파일 형태소 분석 - KOMORAN 사이트 들어가서 형태소 확인
            KomoranResult analyzeResultList = komoran.analyze(content);

            // 형태소 분석 후 토큰중 일반명사, 고유명사만 선택  
            List<Token> tokenList = analyzeResultList.getTokenList();
            for (Token token : tokenList) {   // 명사와 대명사만 가져오기
            	if (token.getPos().equals("NNG") ||  token.getPos().equals("NNP")) {
            		map.put(token.getMorph(), map.getOrDefault(token.getMorph(),0)+1);
            	}
            }
        } 
        // 명사 추출 결과 출력
//		System.out.format("{");
		for (Entry<String, Integer> entrySet : map.entrySet()) {
			System.out.println(entrySet.getKey() + "\t" + entrySet.getValue());
			//System.out.format("'%s' : %d, ", entrySet.getKey(),entrySet.getValue());
		} 
//		System.out.format("}) \n");
		
        System.out.println(strDirPath + "> 폴더의 파일 갯수 >> " + fList.length); 
		System.out.println("단어 수 >> " + map.size());

    } 
   
}
