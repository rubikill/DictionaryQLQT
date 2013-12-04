package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import android.os.Environment;

public class FuzzyDAO implements IFuzzyDAO {

	@SuppressWarnings({ "deprecation", "resource" })
	@Override
	public ArrayList<String> search(String strText) {
		ArrayList<String> listText = new ArrayList<String>();
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/data/fuzzy_index");
			Directory fsDirectory;
			fsDirectory = FSDirectory.open(file);
			IndexReader indexReader = IndexReader.open(fsDirectory);
			Searcher indexSearcher = new IndexSearcher(indexReader);

			// tao query
			Query query = new FuzzyQuery(new Term("word", strText));

			// tien hanh truy van
			TopDocs topDocs = indexSearcher.search(query, 5);
			ScoreDoc[] scoreDosArray = topDocs.scoreDocs;
			// kiem tra xem ket qua tim duoc
			if (scoreDosArray.length != 0) {
				// lay word
				for (ScoreDoc scoredoc : scoreDosArray) {
					Document doc = indexSearcher.doc(scoredoc.doc);
					String strWord = doc.getField("word").stringValue();
					listText.add(strWord);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listText;
	}

}
