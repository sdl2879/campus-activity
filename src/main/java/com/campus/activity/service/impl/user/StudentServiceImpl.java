package com.campus.activity.service.impl.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSON;
import com.campus.activity.entity.sys.dto.ScoreAdjustDTO;
import com.campus.activity.entity.sys.dto.StudentAddDTO;
import com.campus.activity.entity.sys.dto.StudentQueryDTO;
import com.campus.activity.entity.sys.dto.StudentUpdateDTO;
import com.campus.activity.entity.user.Student;
import com.campus.activity.entity.user.StudentScoreLog;
import com.campus.activity.entity.user.SysAdminOperationLog;
import com.campus.activity.mapper.user.StudentMapper;
import com.campus.activity.mapper.user.StudentScoreLogMapper;
import com.campus.activity.mapper.user.SysAdminOperationLogMapper;
import com.campus.activity.service.user.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理服务实现类
 * - 所有写操作均开启事务（@Transactional）
 * - 自动记录管理员操作日志
 * - 分值调整保证原子性（通过数据库 update + 操作）
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentScoreLogMapper scoreLogMapper;

    @Autowired
    private SysAdminOperationLogMapper logMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ==================== 查询类方法 ====================

    @Override
    public IPage<Student> getStudentPage(StudentQueryDTO query) {
        Page<Student> page = new Page<>(query.getPageNum(), query.getPageSize());
        return studentMapper.selectStudentPage(page, query);
    }

    @Override
    public Student getStudentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        return studentMapper.selectById(id);
    }

    @Override
    public Student getStudentByNo(String studentNo) {
        if (studentNo == null || studentNo.trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        return studentMapper.selectByStudentNo(studentNo);
    }

    @Override
    public List<Student> exportStudentList(StudentQueryDTO query) {
        if (query == null) {
            query = new StudentQueryDTO();
        }
        // 使用最大分页避免内存溢出（实际建议限制导出条数）
        Page<Student> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<Student> studentPage = studentMapper.selectStudentPage(page, query);
        return studentPage.getRecords();
    }

    @Override
    public List<Student> listByDeptId(Long deptId, Integer status) {
        if (deptId == null) {
            throw new IllegalArgumentException("院系ID不能为空");
        }
        return studentMapper.selectByDeptId(deptId, status);
    }

    @Override
    public IPage<StudentScoreLog> getScoreLogPage(Long studentId, Long activityId, Integer pageNum, Integer pageSize) {
        Page<StudentScoreLog> page = new Page<>(pageNum, pageSize);
        return scoreLogMapper.selectScoreLogPage(page, studentId, activityId);
    }

    // ==================== 写操作（带事务）====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addStudent(StudentAddDTO addDTO, Long operatorId, String operatorName, String ip) {
        // 唯一性校验
        Student exist = studentMapper.selectByStudentNo(addDTO.getStudentNo());
        if (exist != null) {
            throw new RuntimeException("学号已存在");
        }

        // 密码加密
        String encryptPwd = passwordEncoder.encode(addDTO.getPassword());

        // 构建学生实体
        Student student = new Student();
        student.setStudentNo(addDTO.getStudentNo());
        student.setUsername(addDTO.getStudentNo()); // 默认账号=学号
        student.setPassword(encryptPwd);
        student.setName(addDTO.getName());
        student.setGender(addDTO.getGender());
        student.setDeptId(addDTO.getDeptId());
        student.setDeptName(addDTO.getDeptName());
        student.setMajorId(addDTO.getMajorId());
        student.setMajorName(addDTO.getMajorName());
        student.setGrade(addDTO.getGrade());           // 如 "2023级"
        student.setClassName(addDTO.getClassName());
        student.setPhone(addDTO.getPhone());
        student.setEmail(addDTO.getEmail());
        student.setAvatar(addDTO.getAvatar());
        student.setInterestTags(addDTO.getInterestTags() == null ? "" : addDTO.getInterestTags());
        student.setTotalScore(BigDecimal.ZERO);        // 初始分值为0
        student.setStatus(1);                          // 默认启用
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());

        studentMapper.insert(student);

        // 记录操作日志
        recordLog(operatorId, operatorName, "新增", "学生管理", JSON.toJSONString(addDTO), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStudent(StudentUpdateDTO updateDTO, Long operatorId, String operatorName, String ip) {
        Student student = studentMapper.selectById(updateDTO.getId());
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        // 更新字段
        student.setName(updateDTO.getName());
        student.setGender(updateDTO.getGender());
        student.setDeptId(updateDTO.getDeptId());
        student.setDeptName(updateDTO.getDeptName());
        student.setMajorId(updateDTO.getMajorId());
        student.setMajorName(updateDTO.getMajorName());
        student.setGrade(updateDTO.getGrade());
        student.setClassName(updateDTO.getClassName());
        student.setPhone(updateDTO.getPhone());
        student.setEmail(updateDTO.getEmail());
        student.setAvatar(updateDTO.getAvatar());
        student.setInterestTags(updateDTO.getInterestTags() == null ? "" : updateDTO.getInterestTags());
        student.setStatus(updateDTO.getStatus());
        student.setUpdateTime(LocalDateTime.now());

        studentMapper.updateById(student);
        recordLog(operatorId, operatorName, "编辑", "学生管理", JSON.toJSONString(updateDTO), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(Long id, Integer status, Long operatorId, String operatorName, String ip) {
        if (id == null || status == null || (status != 0 && status != 1)) {
            throw new IllegalArgumentException("参数错误：ID不能为空，状态仅支持0（禁用）或1（启用）");
        }

        Student student = studentMapper.selectById(id);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        if (student.getStatus().equals(status)) {
            throw new RuntimeException("当前状态已为" + (status == 1 ? "启用" : "禁用") + "，无需操作");
        }

        student.setStatus(status);
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.updateById(student);

        String operation = status == 1 ? "启用" : "禁用";
        Map<String, Object> logData = new HashMap<>();
        logData.put("id", id);
        logData.put("status", status);
        recordLog(operatorId, operatorName, operation, "学生管理", JSON.toJSONString(logData), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetPassword(Long id, Long operatorId, String operatorName, String ip) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        String defaultPwd = "123456";
        String encryptPwd = passwordEncoder.encode(defaultPwd);
        student.setPassword(encryptPwd);
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.updateById(student);

        Map<String, Object> logData = new HashMap<>();
        logData.put("id", id);
        logData.put("password", "******"); // 隐藏密码
        recordLog(operatorId, operatorName, "重置密码", "学生管理", JSON.toJSONString(logData), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adjustScore(ScoreAdjustDTO adjustDTO, Long operatorId, String operatorName, String ip) {
        if (adjustDTO.getStudentId() == null || adjustDTO.getScore() == null) {
            throw new IllegalArgumentException("学生ID和调整分值不能为空");
        }

        Student student = studentMapper.selectById(adjustDTO.getStudentId());
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        // 校验调整后分值非负
        BigDecimal newScore = student.getTotalScore().add(adjustDTO.getScore());
        if (newScore.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("调整后分值不能为负数");
        }

        // 原子更新总分（SQL: UPDATE student SET total_score = total_score + #{score} WHERE id = #{id}）
        studentMapper.updateTotalScore(adjustDTO.getStudentId(), adjustDTO.getScore());

        // 记录分值变动日志
        StudentScoreLog scoreLog = new StudentScoreLog();
        scoreLog.setStudentId(adjustDTO.getStudentId());
        scoreLog.setStudentNo(student.getStudentNo());
        scoreLog.setStudentName(student.getName());
        scoreLog.setActivityId(adjustDTO.getActivityId() == null ? 0L : adjustDTO.getActivityId());
        scoreLog.setActivityName(adjustDTO.getActivityName() == null ? "" : adjustDTO.getActivityName());
        scoreLog.setScore(adjustDTO.getScore());
        scoreLog.setType(adjustDTO.getType()); // 如：1-活动加分，2-人工调整
        scoreLog.setRemark(adjustDTO.getRemark() == null ? "" : adjustDTO.getRemark());
        scoreLog.setOperatorId(operatorId);
        scoreLog.setOperatorName(operatorName);
        scoreLog.setCreateTime(LocalDateTime.now());
        scoreLogMapper.insert(scoreLog);

        // 记录管理员操作日志
        recordLog(operatorId, operatorName, "分值调整", "学生管理", JSON.toJSONString(adjustDTO), ip);

        return true;
    }

    // ==================== 工具方法 ====================

    /**
     * 记录管理员操作日志
     */
    private void recordLog(Long operatorId, String operatorName, String operation,
                           String module, String detail, String ip) {
        SysAdminOperationLog log = new SysAdminOperationLog();
        log.setAdminId(operatorId);
        log.setAdminName(operatorName);
        log.setOperation(operation);
        log.setModule(module);
        log.setIp(ip);
        log.setDetail(detail);
        log.setOperationTime(LocalDateTime.now());
        logMapper.insert(log);
    }
}