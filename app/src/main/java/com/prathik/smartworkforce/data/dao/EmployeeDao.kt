package com.prathik.smartworkforce.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prathik.smartworkforce.data.entities.EmployeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees WHERE organizationId = :organizationId")
    fun getAllEmployees(organizationId: Long): Flow<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: EmployeeEntity)

    @Update
    suspend fun update(employee: EmployeeEntity)

    @Delete
    suspend fun delete(employee: EmployeeEntity)

    @Query("SELECT * FROM employees WHERE id = :employeeId")
    fun getEmployeeById(employeeId: String): Flow<EmployeeEntity?>

    @Query("SELECT * FROM employees WHERE email = :email LIMIT 1")
    suspend fun getEmployeeByEmail(email: String): EmployeeEntity?

    @Query("SELECT * FROM employees WHERE approvalStatus = 'pending' AND organizationId = :organizationId")
    fun getPendingEmployees(organizationId: Long): Flow<List<EmployeeEntity>>

    @Query("UPDATE employees SET approvalStatus = :status WHERE id = :employeeId")
    suspend fun updateEmployeeStatus(employeeId: String, status: String)
}
