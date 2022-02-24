/**
 * The MIT License
 * Copyright © 2021 Heinz Nixdorf Chair for Distributed Information Systems, Friedrich Schiller University Jena
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.uni_jena.cs.fusion.experiment.rdf_datatype_usage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.PropertyConfigurator;
import org.h2.tools.Csv;

import de.uni_jena.cs.fusion.experiment.rdf_datatype_usage.utils.H2Util;

/**
 * After the measure to export the database as csv files
 */
public class H2CreateCSVFiles {

	private static org.slf4j.Logger log;

	private static Map<String, String> queries = Map.of(//
			"measurements",
			String.format(
					"SELECT NAME AS CATEGORY, URL AS FILE_URL, MEASUREMENT, PROPERTY, DATATYPE, QUANTITY FROM %s NATURAL JOIN %s NATURAL JOIN %s",
					H2Util.RESULT_DATABASE_TABLE, H2Util.FILE_DATABASE_TABLE, H2Util.CATEGORY_DATABASE_TABLE), //
			"errors",
			String.format(
					"SELECT NAME AS CATEGORY, URL AS FILE_URL, LINE, ERROR_MESSAGE FROM %s NATURAL JOIN %s NATURAL JOIN %s",
					H2Util.ERROR_DATABASE_TABLE, H2Util.FILE_DATABASE_TABLE, H2Util.CATEGORY_DATABASE_TABLE));

	private static File folder = new File("csv_results");

	public static void main(String[] args) {
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		log = org.slf4j.LoggerFactory.getLogger("H2CreateCSVFiles");

		folder.mkdir();

		log.info("Start connection to the database");
		try (Connection con = DriverManager.getConnection(H2Util.DB_URL, H2Util.USER, H2Util.PASS);
				Statement stmt = con.createStatement();) {
			for (String fileName : queries.keySet()) {
				try (Writer writer = new OutputStreamWriter(
						new GZIPOutputStream(new FileOutputStream(new File(folder, fileName + ".csv.gz")), true),
						StandardCharsets.UTF_8)) {
					log.info("Exporting " + fileName + " as csv file");
					try (ResultSet result = stmt.executeQuery(queries.get(fileName))) {
						new Csv().write(writer, result);
					} catch (SQLException e) {
						log.error(e.getMessage());
						System.exit(1);
					}
				} catch (IOException e) {
					log.error(e.getMessage());
					System.exit(1);
				}
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			System.exit(1);
		}
	}

}
