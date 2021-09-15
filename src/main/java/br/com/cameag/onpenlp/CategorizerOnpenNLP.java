package br.com.cameag.onpenlp;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class CategorizerOnpenNLP {
	
	public static TokenNameFinderModel 	tokenNameFinderModel;
	public static TokenizerModel 		tokenizerModel;
	public static SentenceModel		 	sentenceModel;
	
	
	public CategorizerOnpenNLP() {
		
		InputStream is = null;
		try {
			is = new FileInputStream("model/en-ner-person.bin");
		 	tokenNameFinderModel = new TokenNameFinderModel(is);
			is.close();
			
			is = new FileInputStream("model/pt-token.bin");
		 	tokenizerModel = new TokenizerModel(is);
		 	is.close();
			
		 	is = new FileInputStream("model/pt-sent.bin");
		 	sentenceModel = new SentenceModel(is);
			is.close();
		 	
		}catch (Exception e) {
			System.out.println("Error: ".concat(e.getMessage()));
			System.exit(0);
		}
	}
	
	public Integer findName(String[] sentence) throws Exception {
		
		NameFinderME nameFinder = new NameFinderME(tokenNameFinderModel);
        Span nameSpans[] = nameFinder.find(sentence);
	    
        Integer ret = null;
        for(Span s: nameSpans) {
	    	ret = s.getStart();
	    	break;
        }
        return ret;
	}
	
	public String tokenize(String variavel) throws Exception {
		
		Tokenizer tokenizer = new TokenizerME(tokenizerModel);
	 	String tokens[] = tokenizer.tokenize(variavel);
	 	
	 	Integer retorno = findName(tokens);
	 	if(retorno != null) {
	 		return tokens[retorno];
	 	}
	 	return null;
	}
	
	public static void main(String[] args) throws Exception{
		
		CategorizerOnpenNLP findNameEn = new CategorizerOnpenNLP();
		
		String paragraph = "Bom dia, pessoal. Tudo bem? Aqui quem fala é o Carlos. Estamos atrasado no projeto esperando pelo Mike a aprovação. Danny, tem alguma sugestão? Quero falar mais tarde com Marc.";
		 
		SentenceDetectorME sdetector = new SentenceDetectorME(sentenceModel);
	 	
		String[] sentences = sdetector.sentDetect(paragraph);
		for(String s : sentences) {
			String nome = findNameEn.tokenize(s);
			System.out.println(s +" "+ (nome!=null?"<< nome detectado: ".concat(nome).concat(" >>"):""));
		}
		
	}

}