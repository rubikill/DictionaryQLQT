package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

public class IndexerImpl implements IIndexer {
	private IndexWriter indexWriter;
	private Context context;

	public IndexerImpl(Context context) {
		this.context = context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void createIndexWriter() {
		if (indexWriter == null) {
			try {
				// Create instance of Directory where index files will be stored
				/*
				 * Create instance of analyzer, which will be used to tokenize
				 * the input data
				 */

				File f = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/fuzzydata/");
				f.mkdir();
				Directory fsDirectory = FSDirectory.open(f);
				Analyzer standardAnalyzer = new StandardAnalyzer(
						Version.LUCENE_34);
				// Create a new index
				boolean create = true;
				// Create the instance of deletion policy
				IndexDeletionPolicy deletionPolicy = new KeepOnlyLastCommitDeletionPolicy();
				indexWriter = new IndexWriter(fsDirectory, standardAnalyzer,
						create, deletionPolicy,
						IndexWriter.MaxFieldLength.UNLIMITED);

			} catch (IOException ie) {
				System.out.println("Error in creating IndexWriter");
				throw new RuntimeException(ie);
			}
		}

	}

	@SuppressLint("DefaultLocale")
	@Override
	public void indexData() {
		InputStream is;
		try {
			is = context.getAssets().open("EnglishVietnamese.index");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			String str = "";
			boolean flag_dangdoc = true;
			while (flag_dangdoc) {
				str = reader.readLine();
				if (str == null) {
					flag_dangdoc = false;
					break;
				}
				String[] list = str.split("\t");
				String strword = list[0];
				String strindex = list[1];
				String strlen = list[2];

				Field wordfield = new Field("word", strword, Field.Store.YES,
						Field.Index.ANALYZED);
				if (strword.toLowerCase().indexOf("pune") != -1) {
					// Display search results that contain pune in their subject
					// first by setting boost factor
					wordfield.setBoost(2.2F);
				}

				Field indexfield = new Field("index", strindex,
						Field.Store.YES, Field.Index.ANALYZED);

				Field lenfield = new Field("len", strlen, Field.Store.YES,
						Field.Index.ANALYZED);
				Document doc = new Document();
				doc.add(wordfield);
				doc.add(indexfield);
				doc.add(lenfield);
				indexWriter.addDocument(doc);
			}
			indexWriter.optimize();
			indexWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
