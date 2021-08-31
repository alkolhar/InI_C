var vm = new Vue({

  el: '#SendMailVue',
  data: {
      mailServer: 'mail.ntb.ch',
      from:       '',
      to:         '',
      subject:    '',
      text:       'This space for rent.',
      message:    ''
  }, 
  methods: {
      send: function(event) {
   	     var url = "/WebMailClientVue/SendMail?MailServer=" + escape(this.mailServer) + 
		   "&FromAddress=" + escape(this.from) + 
		   "&ToAddress=" + escape(this.to) +
		   "&Subject=" + escape(this.subject) +
		   "&Text=" + escape(this.text);
   	     axios.get(url).then((response) => { 
   	    	 this.message = response.data; 
   	     }, (error) => {
   	    	 this.message = 'Unable to send mail';
   	     });
      },
      clear: function(event) {
    	  this.mailServer = '';
    	  this.from = '';
    	  this.to = '';
    	  this.subject = '';
    	  this.text = '';
    	  this.message = '';
      }
  
  }

});