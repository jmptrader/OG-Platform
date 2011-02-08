/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */

#include "stdafx.h"

// Test the functions and objects in Connector/Alert.cpp

#include "Connector/Alert.h"

LOGGING (com.opengamma.language.connector.AlertTest);

#define TIMEOUT_ALERT		1000

static void EnableDisable () {
	ASSERT (CAlert::Enable ());
	CAlert::Good (TEXT ("This is a good alert"));
	CThread::Sleep (TIMEOUT_ALERT);
	CAlert::Bad (TEXT ("This is a bad alert"));
	CThread::Sleep (TIMEOUT_ALERT);
	ASSERT (CAlert::Disable ());
}

BEGIN_TESTS (AlertTest)
	TEST (EnableDisable)
END_TESTS