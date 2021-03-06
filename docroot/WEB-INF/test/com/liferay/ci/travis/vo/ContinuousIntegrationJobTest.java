/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.ci.travis.vo;

import static org.fest.assertions.Assertions.assertThat;

import com.liferay.ci.portlet.TravisIntegrationConstants;
import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.travis.util.PortletPropsUtil;
import com.liferay.ci.util.TestPropsUtil;

import java.util.Arrays;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Manuel de la Peña
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PortletPropsUtil.class})
public class ContinuousIntegrationJobTest {

	@Test
	public void testConstructor() {
		ContinuousIntegrationJob job = new ContinuousIntegrationJob(
			"account", "unstableJobName", "unstableJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		assertThat(job.getBuildUrl()).isEqualTo(
			"https://travis-ci.org/account/unstableJobName");
	}

	@Test
	public void testCompare1() {
		ContinuousIntegrationJob unstableJob = new ContinuousIntegrationJob(
			"account", "unstableJobName", "unstableJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob failedJob = new ContinuousIntegrationJob(
			"account", "failedJobName", "failedJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob[] sortedJobs = sort(
			new ContinuousIntegrationJob[] {unstableJob, failedJob});

		assertThat(
			sortedJobs[0].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[1].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
	}

	@Test
	public void testCompare2() {
		ContinuousIntegrationJob unstableJob = new ContinuousIntegrationJob(
			"account", "unstableJobName", "unstableJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob failedJob = new ContinuousIntegrationJob(
			"account", "failedJobName", "failedJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob[] sortedJobs = sort(
			new ContinuousIntegrationJob[] {failedJob, unstableJob});

		assertThat(
			sortedJobs[0].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[1].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
	}

	@Test
	public void testCompare3() {
		ContinuousIntegrationJob successJob = new ContinuousIntegrationJob(
			"account", "successJobName", "successJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);

		ContinuousIntegrationJob failedJob = new ContinuousIntegrationJob(
			"account", "failedJobName", "failedJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob[] sortedJobs = sort(failedJob, successJob);

		assertThat(
			sortedJobs[0].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[1].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);
	}

	@Test
	public void testCompare4() {
		ContinuousIntegrationJob successJob = new ContinuousIntegrationJob(
			"account", "successJobName", "successJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);

		ContinuousIntegrationJob unstableJob = new ContinuousIntegrationJob(
			"account", "unstableJobName", "unstableJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob failedJob = new ContinuousIntegrationJob(
			"account", "failedJobName", "failedJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob[] sortedJobs = sort(
			failedJob, successJob, unstableJob);

		assertThat(
			sortedJobs[0].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[1].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[2].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);
	}

	@Test
	public void testCompare5() {
		ContinuousIntegrationJob successJob = new ContinuousIntegrationJob(
			"account", "successJobName", "successJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);

		ContinuousIntegrationJob abortedJob = new ContinuousIntegrationJob(
			"account", "abortedJobName", "abortedJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob unstableJob = new ContinuousIntegrationJob(
			"account", "unstableJobName", "unstableJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob failedJob = new ContinuousIntegrationJob(
			"account", "failedJobName", "failedJob",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);

		ContinuousIntegrationJob[] sortedJobs = sort(
			successJob, abortedJob, unstableJob, failedJob);

		assertThat(
			sortedJobs[0].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[1].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[2].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED);
		assertThat(
			sortedJobs[3].getLastBuildStatus()).isEqualTo(
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);
	}

	@Test
	public void testCompare6() {
		ContinuousIntegrationJob successJob1 = new ContinuousIntegrationJob(
			"account", "successJobName1", "successAliasJob1",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);

		ContinuousIntegrationJob successJob2 = new ContinuousIntegrationJob(
			"account", "successJobName2", "successAliasJob2",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);

		ContinuousIntegrationJob[] sortedJobs = sort(successJob1, successJob2);

		PowerMockito.mockStatic(PortletPropsUtil.class);

		TestPropsUtil.mockPortletKey(PortletPropsKeys.TRAVIS_BASE_API_URL);
		TestPropsUtil.mockPortletKey(
			PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME);

		assertThat(sortedJobs[0].getJobName()).isEqualTo("successJobName1");
		assertThat(sortedJobs[1].getJobName()).isEqualTo("successJobName2");
	}

	@Test
	public void testCompare7() {
		ContinuousIntegrationJob successJob1 = new ContinuousIntegrationJob(
			"account", "successJobName1", "successJob1",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);

		ContinuousIntegrationJob successJob2 = new ContinuousIntegrationJob(
			"account", "successJobName2", "successJob2",
			TravisIntegrationConstants.TRAVIS_BUILD_STATUS_SUCCESS);

		ContinuousIntegrationJob[] sortedJobs = sort(successJob2, successJob1);

		PowerMockito.mockStatic(PortletPropsUtil.class);

		TestPropsUtil.mockPortletKey(PortletPropsKeys.TRAVIS_BASE_API_URL);
		TestPropsUtil.mockPortletKey(
			PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME);

		assertThat(sortedJobs[0].getJobName()).isEqualTo("successJobName1");
		assertThat(sortedJobs[1].getJobName()).isEqualTo("successJobName2");
	}

	protected ContinuousIntegrationJob[] sort(
		ContinuousIntegrationJob... jobs) {

		Arrays.sort(jobs);

		return jobs;
	}

}