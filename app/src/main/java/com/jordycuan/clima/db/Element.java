package com.jordycuan.clima.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JordyCuan on 19/06/16.
 */
@Table(name = "Element")
public class Element extends Model {
	/* BD MODEL EXAMPLE
	"description": "clear sky",
	"temp": 279.388,
	"pressure": 1039.04,
	"humidity": 91,
	"temp_min": 279.388,
	"temp_max": 279.388,
	"speed": 0.58,
	"country": "AR",
	"sunrise": 1466249213,
	"sunset": 1466283253
	"name": "Bahia Blanca"	*/

	@Column(name = "_id", unique = true, onUniqueConflict = Column.ConflictAction.IGNORE) public long _id;
	@Column(name = "Description") public String description;
	@Column(name = "Temp") public int temp;
	@Column(name = "Pressure") public double pressure;
	@Column(name = "Humidity") public int humidity;
	@Column(name = "Temp_min") public int temp_min;
	@Column(name = "Temp_max") public int temp_max;
	@Column(name = "Speed") public double speed;
	@Column(name = "Country") public String country;
	@Column(name = "Sunrise") public long sunrise;
	@Column(name = "Sunset") public long sunset;
	@Column(name = "City_name") public String name;

	@Column(name = "DateString") public String dateString;
	@Column(name = "Date") public long date;

	public Element() { super(); }

	public Element(long _id, String description, int temp, double pressure, int humidity, int temp_min,
	               int temp_max, double speed, String country, long sunrise, long sunset,
	               String name, String dateString, long date) {
		super();
		this._id = _id;
		this.description = description;
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		this.temp_min = temp_min;
		this.temp_max = temp_max;
		this.speed = speed;
		this.country = country;
		this.sunrise = sunrise;
		this.sunset = sunset;
		this.name = name;
		this.dateString = dateString;
		this.date = date;
	}

	@Override
	public String toString() {
		return "Element{" +
				//"_id=" + _id +
				", description='" + description + '\'' +
				", dateString=" + dateString +
				", temp=" + temp +
				", pressure=" + pressure +
				", humidity=" + humidity +
				", temp_min=" + temp_min +
				", temp_max=" + temp_max +
				", speed=" + speed +
				", country='" + country + '\'' +
				", sunrise=" + sunrise +
				", sunset=" + sunset +
				", name='" + name + '\'' +
				'}';
	}

	public static List<Element> getAll() {
		return new Select()
				.from(Element.class)
				.orderBy("date DESC")
				.execute();
	}

	public static List<String> getAllString() {
		List<String> list = new ArrayList<>();
		for (Element e : Element.getAll()) {
			list.add(e.toString());
		}
		return list;
	}

	public static Element getElementById(long id) {
		return new Select()
				.from(Element.class)
				.where("_id = ?", id)
				.executeSingle();
	}
}
