/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Alicia\\Desktop\\Kicks\\src\\com\\kicks\\ChronometerServiceInterface.aidl
 */
package com.kicks;
public interface ChronometerServiceInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.kicks.ChronometerServiceInterface
{
private static final java.lang.String DESCRIPTOR = "com.kicks.ChronometerServiceInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.kicks.ChronometerServiceInterface interface,
 * generating a proxy if needed.
 */
public static com.kicks.ChronometerServiceInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.kicks.ChronometerServiceInterface))) {
return ((com.kicks.ChronometerServiceInterface)iin);
}
return new com.kicks.ChronometerServiceInterface.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getNumberOfKicks:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getNumberOfKicks();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getStartTime:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getStartTime();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_setKicks:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setKicks(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setStartTime:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.setStartTime(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_incrementKicks:
{
data.enforceInterface(DESCRIPTOR);
this.incrementKicks();
reply.writeNoException();
return true;
}
case TRANSACTION_setIsFirst:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setIsFirst(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getIsFirst:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getIsFirst();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_enableNotification:
{
data.enforceInterface(DESCRIPTOR);
this.enableNotification();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.kicks.ChronometerServiceInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public int getNumberOfKicks() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getNumberOfKicks, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public long getStartTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getStartTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setKicks(int kicks) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(kicks);
mRemote.transact(Stub.TRANSACTION_setKicks, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setStartTime(long startTime) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(startTime);
mRemote.transact(Stub.TRANSACTION_setStartTime, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void incrementKicks() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_incrementKicks, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setIsFirst(boolean isF) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isF)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setIsFirst, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean getIsFirst() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getIsFirst, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void enableNotification() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enableNotification, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getNumberOfKicks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getStartTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setKicks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setStartTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_incrementKicks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setIsFirst = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getIsFirst = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_enableNotification = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
public int getNumberOfKicks() throws android.os.RemoteException;
public long getStartTime() throws android.os.RemoteException;
public void setKicks(int kicks) throws android.os.RemoteException;
public void setStartTime(long startTime) throws android.os.RemoteException;
public void incrementKicks() throws android.os.RemoteException;
public void setIsFirst(boolean isF) throws android.os.RemoteException;
public boolean getIsFirst() throws android.os.RemoteException;
public void enableNotification() throws android.os.RemoteException;
}
