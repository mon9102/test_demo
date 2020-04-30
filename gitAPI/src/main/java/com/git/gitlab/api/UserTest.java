package com.git.gitlab.api;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.Query;
import org.gitlab.api.models.*;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author : zouren
 * @date : 2020/4/29 12:23
 */
public class UserTest {
    String host = "";
    String user = "";
    String pass = "";

    public void init() {

    }

    private GitlabSession getGitlabSession() throws Exception {

        return GitlabAPI.connect(host, user, pass);
    }

    private GitlabAPI getGitlabAPI() throws Exception {
        String apiToken = getGitlabSession().getPrivateToken();
        return GitlabAPI.connect(host, apiToken);
    }

    /**
     * ⽤户信息获取
     *
     * @throws Exception
     */
    public void userList() throws Exception {
        GitlabAPI gitlabAPI = getGitlabAPI();
        //所有用户
        List<GitlabUser> gitlabUserList = gitlabAPI.getUsers();
        //组用户
        List<GitlabGroup> gitlabGroupList = gitlabAPI.getGroups();
        List<GitlabGroupMember> gitlabGroupMembers = gitlabGroupList.stream()
                .flatMap(gitlabGroup -> gitlabAPI.getGroupMembers(gitlabGroup.getId()).stream())
                .collect(Collectors.toList());
        //项目用户
        gitlabAPI.getProjects().stream().flatMap(gitlabProject -> {
            try {
                return gitlabAPI.getProjectMembers(gitlabProject.getId()).stream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    /**
     * 分⽀获取
     */
    public void projectInfo() throws Exception {
        GitlabAPI gitlabAPI = getGitlabAPI();

        List<GitlabProject> gitlabProjectList =
                gitlabAPI.getAllProjects();
        List<GitlabBranch> gitlabBranchList = gitlabProjectList.stream()
                .flatMap(gitlabProject -> gitlabAPI.getBranches(gitlabProject).stream())
                .collect(Collectors.toList());
    }

    /**
     * 提交信息获取
     */
    public void commitInfo() throws Exception {
        //全量获取
        GitlabAPI gitlabAPI = getGitlabAPI();

        List<GitlabProject> gitlabProjectList =
                gitlabAPI.getAllProjects();
        Map gitlabBranchList = gitlabProjectList.stream()
                .flatMap(gitlabProject -> {
                    Map<Integer, List<GitlabBranch>> map = new HashMap<Integer, List<GitlabBranch>>();
                    map.put(gitlabProject.getId(), gitlabAPI.getBranches(gitlabProject));
                    return map.entrySet().stream();
                }).collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
        //项目id
        Integer projectId = 0;
        //分支名
        String branchName = null;
        List<GitlabCommit> gitlabCommitList =
                gitlabAPI.getAllCommits(projectId,
                        branchName);
    }

    /**
     * 条件获取（按照提交时间）
     */
    private List<GitlabCommit> getCommitByCommiteDate(Integer projectId,
                                                      String branchOrTag, String calDate) throws Exception {
        GitlabAPI gitlabAPI = getGitlabAPI();
        Query query = new Query();
        if (branchOrTag != null) {
            query.append("ref_name", branchOrTag);
        }
        if (calDate != null) {
            LocalDateTime dateTime = LocalDateTime.parse(calDate);
            query.append("since", dateTime.toString());
        }
        String tailUrl = GitlabProject.URL + "/" +
                projectId +
                "/repository" + GitlabCommit.URL + query;
        GitlabCommit[] commits =
                gitlabAPI.retrieve().to(tailUrl, GitlabCommit[].class);
        return Arrays.asList(commits);
    }

    /**
     * 条件获取（按照提交时间）
     * @throws Exception
     */
    public void getCommitByCommiteDateTest() throws Exception{
        //项目id
        Integer projectId = 0;
        //分支名
        String branchName = null;
        //时间
        String calDate = null;

        LocalDateTime dateTime = LocalDateTime.parse(calDate);
        //调⽤代码
        List<GitlabCommit> gitlabCommitList =
                getCommitByCommiteDate(projectId,
                        branchName,dateTime.toString());
    }

}
