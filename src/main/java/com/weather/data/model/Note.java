package com.weather.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "NOTE")
public class Note implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8309233351438775539L;

	@Id
	@GenericGenerator(name = "note", strategy = "increment")
	@GeneratedValue(generator = "note")
	@Column(name = "id")
	private long noteId;
		
	@Column(name = "note_body", nullable = false)
	private String noteBody;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "note_date")
	private Date noteDate;

	@PrePersist
	protected void onCreate() {
		if (noteDate == null) {
			noteDate = new Date();
		}
	}
	
	public long getNoteId() {
		return noteId;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}

	public String getNoteBody() {
		return noteBody;
	}

	public void setNoteBody(String noteBody) {
		this.noteBody = noteBody;
	}

	public Date getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(Date noteDate) {
		this.noteDate = noteDate;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", noteBody=" + noteBody + ", noteDate=" + noteDate + "]";
	}
	
}
