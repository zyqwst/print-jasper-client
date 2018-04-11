package com.albert.model;

import net.sf.jasperreports.engine.util.DigestUtils;

public class JasperForPrinter implements Comparable<Object>{
		private String uuid;
		/**Jasper报表名称*/
		private String jasper;
		/**打印机*/
		private String printer;
		/**是否可用*/
		private Boolean status;
		/**是否预览*/
		private Boolean preview;
		
		public JasperForPrinter() {
		}
		public JasperForPrinter(String uuid) {
			this.uuid=uuid;
		}
		public JasperForPrinter(String jasper, String printer,Boolean status,Boolean preview) {
			this.jasper = jasper;
			this.printer = printer;
			this.uuid =DigestUtils.instance().md5(jasper.trim()).toString();
			this.status=status;
			this.preview = preview;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getJasper() {
			return jasper;
		}
		public void setJasper(String jasper) {
			this.jasper = jasper;
		}
		public String getPrinter() {
			return printer;
		}
		public void setPrinter(String printer) {
			this.printer = printer;
		}
		public Boolean getStatus() {
			return status;
		}
		public void setStatus(Boolean status) {
			this.status = status;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof JasperForPrinter)) {
				return false;
			}
			JasperForPrinter other = (JasperForPrinter) obj;
			if (uuid == null) {
				if (other.uuid != null) {
					return false;
				}
			} else if (!uuid.equals(other.uuid)) {
				return false;
			}
			return true;
		}

		
		@Override
		public int compareTo(Object o) {
			return this.jasper.compareTo(((JasperForPrinter)o).getJasper());
		}
		public Boolean getPreview() {
			return preview;
		}
		public void setPreview(Boolean preview) {
			this.preview = preview;
		}
	}