package com.weather.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.weather.data.enums.PreDefinedNoteRangeEnum;

@Entity
@Table(name = "predefined_note")
public class PredefinedNote implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7209948889687298436L;

	@Id
	@GenericGenerator(name = "predefined_note", strategy = "increment")
	@GeneratedValue(generator = "predefined_note")
	@Column(name = "id")
	private long noteId;
		
	@Column(name = "note_body", nullable = false)
	private String noteBody;
	
	@Column(name = "from_degree", nullable = false)
	private int fromDegree;
	
	@Column(name = "to_degree", nullable = false)
	private int toDegree;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "note_date")
	private Date noteDate;
	
	
	@Column(name = "note_range")
	@Enumerated(EnumType.ORDINAL)
	private PreDefinedNoteRangeEnum noteRange;
		
	@PrePersist
	protected void onCreate() {
		if (noteDate == null) {
			noteDate = new Date();
		}
		
		switch (noteRange)
		{
		case PREDIFNED_NOTE_RANGE_0:
			fromDegree = 1;
			toDegree= 10;
			break;
		
		case PREDIFNED_NOTE_RANGE_1:
			fromDegree = 10;
			toDegree= 15;
			break;
			
		case PREDIFNED_NOTE_RANGE_2:			
			fromDegree = 15;
			toDegree= 20;
			break;

		case PREDIFNED_NOTE_RANGE_3:			
			fromDegree = 20;
			toDegree= Integer.MAX_VALUE;
			break;			
			
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

	public int getFromDegree() {
		return fromDegree;
	}

	public void setFromDegree(int fromDegree) {
		this.fromDegree = fromDegree;
	}

	public int getToDegree() {
		return toDegree;
	}

	public void setToDegree(int toDegree) {
		this.toDegree = toDegree;
	}

	public Date getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(Date noteDate) {
		this.noteDate = noteDate;
	}
	
	public PreDefinedNoteRangeEnum getNoteRange() {
		return noteRange;
	}

	public void setNoteRange(PreDefinedNoteRangeEnum noteRange) {
		this.noteRange = noteRange;
	}

	@Override
	public String toString() {
		return "PredefinedNote [noteId=" + noteId + ", noteBody=" + noteBody + ", fromDegree=" + fromDegree
				+ ", toDegree=" + toDegree + ", noteDate=" + noteDate + ", noteRange=" + noteRange + "]";
	}
	
}
