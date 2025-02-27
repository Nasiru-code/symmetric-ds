/**
 * Licensed to JumpMind Inc under one or more contributor
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jumpmind.db.platform.db2;

import javax.sql.DataSource;

import org.jumpmind.db.platform.DatabaseNamesConstants;
import org.jumpmind.db.platform.PermissionResult;
import org.jumpmind.db.platform.PermissionType;
import org.jumpmind.db.platform.PermissionResult.Status;
import org.jumpmind.db.sql.SqlException;
import org.jumpmind.db.sql.SqlTemplateSettings;

public class Db2zOsDatabasePlatform extends Db2DatabasePlatform {

    public Db2zOsDatabasePlatform(DataSource dataSource, SqlTemplateSettings settings) {
        super(dataSource, settings);
        if (majorVersion < 10) {
            supportsTruncate = false;
        }
    }
    
    @Override
    public String getName() {
        return DatabaseNamesConstants.DB2ZOS;
    }
    
    @Override
    protected Db2DdlReader createDdlReader() {
        return new Db2zOsDdlReader(this);
    }   
    
    @Override
    protected Db2DdlBuilder createDdlBuilder() {
    	return new Db2zOsDdlBuilder();
    }
    
    
    @Override
    public boolean supportsLimitOffset() {
        return false;
    }

    @Override
    public PermissionResult getCreateSymTriggerPermission() {
        String delimiter = getDatabaseInfo().getDelimiterToken();
        delimiter = delimiter != null ? delimiter : "";
           
        String triggerSql = "CREATE TRIGGER TEST_TRIGGER AFTER UPDATE ON " + PERMISSION_TEST_TABLE_NAME + " FOR EACH ROW MODE DB2SQL BEGIN ATOMIC END"; 

        PermissionResult result = new PermissionResult(PermissionType.CREATE_TRIGGER, triggerSql);
        
        try {
            //getSqlTemplate().update(triggerSql);
            result.setStatus(Status.PASS);
        } catch (SqlException e) {
            result.setException(e);
            result.setSolution("Grant CREATE TRIGGER permission or TRIGGER permission");
        }
        
        return result;
    }
    
    protected PermissionResult getDropSymTriggerPermission() {
        String dropTriggerSql = "DROP TRIGGER IF EXISTS TEST_TRIGGER";
        PermissionResult result = new PermissionResult(PermissionType.DROP_TRIGGER, dropTriggerSql);

        try {
            //getSqlTemplate().update(dropTriggerSql);
            result.setStatus(Status.PASS);
        } catch (SqlException e) {
            result.setException(e);
            result.setSolution("Grant DROP TRIGGER permission or TRIGGER permission");
        }

        return result;
    }
}
