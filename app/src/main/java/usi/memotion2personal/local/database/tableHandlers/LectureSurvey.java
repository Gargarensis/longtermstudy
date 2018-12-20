package usi.memotion2personal.local.database.tableHandlers;

import android.content.ContentValues;
import android.database.Cursor;

import usi.memotion2personal.local.database.db.LocalDbUtility;
import usi.memotion2personal.local.database.db.LocalTables;
import usi.memotion2personal.local.database.tables.LectureSurveyTable;

/**
 * Created by shkurtagashi on 9/11/18.
 */

public class LectureSurvey extends TableHandler{
    private static LocalTables table = LocalTables.TABLE_LECTURE_SURVEY;

    public String _id;
    public String timestamp;
    public int paperId;
    public int question1;
    public int question2;
    public int question3;
    public int question4;
    public int question5;
    public int question6;
    public int question7;
    public int question8;
    public int question9;
    public int question10;
    public String questionnaireType;


    public void setQuestionnaireType(String t){this.questionnaireType = t;}
    public void setTimestamp(String t){
        this.timestamp = t;
    }

    public void setPaperId(int pID) {
        this.paperId = pID;
    }

    public void setId(String id){
        this._id = id;
    }

    public void setQuestion1(int q) {
        this.question1 = q;
    }
    public void setQuestion2(int q) {
        this.question2 = q;
    }
    public void setQuestion3(int q) {
        this.question3 = q;
    }
    public void setQuestion4(int q) {
        this.question4 = q;
    }
    public void setQuestion5(int q) {
        this.question5 = q;
    }
    public void setQuestion6(int q) {
        this.question6 = q;
    }
    public void setQuestion7(int q) {this.question7 = q;}
    public void setQuestion8(int q) {this.question8 = q;}
    public void setQuestion9(int q) {this.question9 = q;}
    public void setQuestion10(int q) {this.question10 = q;}



    public String getTimestamp() {
        return timestamp;
    }
    public String getQuestionnaireType(){return questionnaireType;}
    private String get_id(){
        return this._id;
    }
    public int getQuestion1() {
        return question1;
    }
    public int getQuestion2() {
        return question2;
    }
    public int getQuestion3() {
        return question3;
    }
    public int getQuestion4() {
        return question4;
    }
    public int getQuestion5() {
        return question5;
    }
    public int getQuestion6() {
        return question6;
    }
    public int getQuestion7() {
        return question7;
    }
    public int getQuestion8() {
        return question8;
    }
    public int getQuestion9() {
        return question9;
    }
    public int getQuestion10() {
        return question10;
    }



    public int getPaperId() {
        return paperId;
    }


    public LectureSurvey(boolean isNewRecord) {
        super(isNewRecord);
        id = -1;
        columns = LocalDbUtility.getTableColumns(table);
    }

    public static TableHandler findByPk(long pk) {
        String idColumn = LocalDbUtility.getTableColumns(table)[0];
        Cursor surveyRecord = localController.rawQuery("SELECT * FROM " + LocalDbUtility.getTableName(table) + " WHERE " + idColumn + " = " + pk + " LIMIT " + 1, null);

        if(surveyRecord.getCount() == 0) {
            return null;
        }

        LectureSurvey survey = new LectureSurvey(false);
        surveyRecord.moveToFirst();

        survey.setAttributes(survey.getAttributesFromCursor(surveyRecord));

        surveyRecord.close();
        return survey;
    }

    @Override
    public void setAttributes(ContentValues attributes) {
        if(attributes.containsKey(columns[0])) {
            id = attributes.getAsLong(columns[0]);
        }

        if(attributes.containsKey(columns[1])) {
            timestamp = attributes.getAsString(columns[1]);
        }

        if(attributes.containsKey(columns[2])) {
            question1 = attributes.getAsInteger(columns[2]);
        }

        if(attributes.containsKey(columns[3])) {
            question2 = attributes.getAsInteger(columns[3]);
        }

        if(attributes.containsKey(columns[4])) {
            question3 = attributes.getAsInteger(columns[4]);
        }

        if(attributes.containsKey(columns[5])) {
            question4 = attributes.getAsInteger(columns[5]);
        }

        if(attributes.containsKey(columns[6])) {
            question5 = attributes.getAsInteger(columns[6]);
        }

        if(attributes.containsKey(columns[7])) {
            question6 = attributes.getAsInteger(columns[7]);
        }

        if(attributes.containsKey(columns[8])) {
            question7 = attributes.getAsInteger(columns[8]);
        }

        if(attributes.containsKey(columns[9])) {
            question8 = attributes.getAsInteger(columns[9]);
        }

        if(attributes.containsKey(columns[10])) {
            question9 = attributes.getAsInteger(columns[10]);
        }

        if(attributes.containsKey(columns[11])) {
            question10 = attributes.getAsInteger(columns[11]);
        }

        if(attributes.containsKey(columns[12])) {
            questionnaireType = attributes.getAsString(columns[12]);
        }

    }

    @Override
    public ContentValues getAttributes() {
        ContentValues attributes = new ContentValues();
        if(id >= 0) {
            attributes.put(columns[0], id);
        }
        attributes.put(columns[1], timestamp);
        attributes.put(columns[2], question1);
        attributes.put(columns[3], question2);
        attributes.put(columns[4], question3);
        attributes.put(columns[5], question4);
        attributes.put(columns[6], question5);
        attributes.put(columns[7], question6);
        attributes.put(columns[8], question7);
        attributes.put(columns[9], question8);
        attributes.put(columns[10], question9);
        attributes.put(columns[11], question10);
        attributes.put(columns[12], questionnaireType);


        return attributes;
    }

    public static TableHandler[] findAll(String select, String clause) {
        Cursor surveyRecords = localController.rawQuery("SELECT " + select + " FROM " + LocalDbUtility.getTableName(table) + " WHERE " + clause, null);
        TableHandler[] surveys =  new TableHandler[surveyRecords.getCount()];

        if(surveyRecords.getCount() == 0) {
            return surveys;
        }

        int i = 0;
        LectureSurvey survey;
        while(surveyRecords.moveToNext()) {
            survey = new LectureSurvey(false);

            survey.setAttributes(survey.getAttributesFromCursor(surveyRecords));
            surveys[i] = survey;
            i++;
        }
        surveyRecords.close();
        return surveys;
    }

    public static TableHandler find(String select, String clause) {
        Cursor surveyRecord = localController.rawQuery("SELECT " + select + " FROM " + LocalDbUtility.getTableName(table) + " WHERE " + clause + " LIMIT " + 1, null);

        if(surveyRecord.getCount() == 0) {
            return null;
        }

        LectureSurvey survey = new LectureSurvey(false);
        surveyRecord.moveToFirst();

        survey.setAttributes(survey.getAttributesFromCursor(surveyRecord));

        surveyRecord.close();
        return survey;
    }

    @Override
    public ContentValues getAttributesFromCursor(Cursor cursor) {
        ContentValues attributes = new ContentValues();
        attributes.put(columns[0], cursor.getLong(0));
        attributes.put(columns[1], cursor.getLong(1));
        attributes.put(columns[2], cursor.getInt(2));
        attributes.put(columns[3], cursor.getInt(3));
        attributes.put(columns[4], cursor.getInt(4));
        attributes.put(columns[5], cursor.getInt(5));
        attributes.put(columns[6], cursor.getInt(6));
        attributes.put(columns[7], cursor.getInt(7));
        attributes.put(columns[8], cursor.getInt(8));
        attributes.put(columns[9], cursor.getInt(9));
        attributes.put(columns[10], cursor.getInt(10));
        attributes.put(columns[11], cursor.getInt(11));
        attributes.put(columns[12], cursor.getString(12));

        return attributes;
    }

    @Override
    public void save() {
        String tableName = LocalDbUtility.getTableName(table);

        if(isNewRecord) {
            localController.insertRecord(tableName, getAttributes());
        } else {
            String columnId = columns[0];
            localController.update(tableName, getAttributes(), columnId + " = " + id);
        }
    }

    @Override
    public void setAttribute(String attributeName, ContentValues attribute) {
        super.setAttribute(attributeName, attribute);

        switch(attributeName) {
            case LectureSurveyTable._ID:
                id = attribute.getAsLong(LectureSurveyTable._ID);
                break;
            case LectureSurveyTable.QUESTION_1:
                question1 = attribute.getAsInteger(LectureSurveyTable.QUESTION_1);
                break;
            case LectureSurveyTable.QUESTION_2:
                question2 = attribute.getAsInteger(LectureSurveyTable.QUESTION_2);
                break;
            case LectureSurveyTable.QUESTION_3:
                question3 = attribute.getAsInteger(LectureSurveyTable.QUESTION_3);
                break;
            case LectureSurveyTable.QUESTION_4:
                question4 = attribute.getAsInteger(LectureSurveyTable.QUESTION_4);
                break;
            case LectureSurveyTable.QUESTION_5:
                question5 = attribute.getAsInteger(LectureSurveyTable.QUESTION_5);
                break;
            case LectureSurveyTable.QUESTION_6:
                question6 = attribute.getAsInteger(LectureSurveyTable.QUESTION_6);
                break;
            case LectureSurveyTable.QUESTION_7:
                question7 = attribute.getAsInteger(LectureSurveyTable.QUESTION_7);
                break;
            case LectureSurveyTable.QUESTION_8:
                question8 = attribute.getAsInteger(LectureSurveyTable.QUESTION_8);
                break;
            case LectureSurveyTable.QUESTION_9:
                question9 = attribute.getAsInteger(LectureSurveyTable.QUESTION_9);
                break;
            case LectureSurveyTable.QUESTION_10:
                question10 = attribute.getAsInteger(LectureSurveyTable.QUESTION_10);
            case LectureSurveyTable.QUESTIONNAIRE_TYPE:
                question10 = attribute.getAsInteger(LectureSurveyTable.QUESTIONNAIRE_TYPE);
        }
    }

    @Override
    public String[] getColumns() {
        return columns;
    }

    @Override
    public String getTableName() {
        return table.getTableName();
    }

    @Override
    public void delete() {
        localController.delete(table.getTableName(), columns[0] + " = " + id);
    }
}
