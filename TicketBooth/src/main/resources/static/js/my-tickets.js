// My Tickets dynamic rendering
function formatPrice(v){return new Intl.NumberFormat('vi-VN').format(v) + ' VNĐ';}
function filterTickets(status){
  document.querySelectorAll('.ticket-card').forEach(c=>{
    if(status==='all') {c.style.display='flex';return;}
    const st = c.getAttribute('data-status');
    c.style.display = (st===status)?'flex':'none';
  });
  toggleEmpty();
}
function toggleEmpty(){
  const anyVisible = Array.from(document.querySelectorAll('.ticket-card')).some(c=>c.style.display!=='none');
  const empty = document.querySelector('.empty-tickets');
  if(empty) empty.style.display = anyVisible? 'none':'block';
}
function initTickets(){
  const container = document.getElementById('ticketsContainer');
  if(!container) return;
  const data = window.myTickets || [];
  if(data.length===0){toggleEmpty();return;}
  const grouped = {};
  data.forEach(t=>{ // group by orderCode
    const key = t.orderCode || 'NO_ORDER';
    if(!grouped[key]) grouped[key] = {tickets:[], meta:t};
    grouped[key].tickets.push(t);
  });
  let html='';
  Object.values(grouped).forEach(group=>{
    const first = group.meta;
    const seatList = group.tickets.map(x=>x.position).join(', ');
    const total = group.tickets.reduce((s,x)=>s+x.price,0);
    // status heuristic: (no actual status field yet) => upcoming placeholder
    const status = 'upcoming';
    html += `<div class="ticket-card" data-status="${status}">`
      + `<div class=\"ticket-poster\"><img src=\"${first.thumbnailUrl || '/images/qr-placeholder.png'}\" alt=\"${first.movieTitle}\"></div>`
      + `<div class=\"ticket-details\">`
        + `<h3 class=\"movie-title\">${first.movieTitle}</h3>`
        + `<div class=\"ticket-info\">`
          + `<div class=\"info-row\"><i class=\"fas fa-calendar-day\"></i><span>${formatDate(first.showDate)} - ${first.startTime}</span></div>`
          + `<div class=\"info-row\"><i class=\"fas fa-map-marker-alt\"></i><span>${first.hallName}</span></div>`
          + `<div class=\"info-row\"><i class=\"fas fa-couch\"></i><span>Ghế: ${seatList}</span></div>`
          + `<div class=\"info-row\"><i class=\"fas fa-money-bill\"></i><span>${formatPrice(total)}</span></div>`
        + `</div>`
      + `</div>`
      + `<div class=\"ticket-actions\">`
        + `<span class=\"ticket-status ${status}\">Sắp tới</span>`
      + `</div>`
    + `</div>`;
  });
  container.innerHTML = html;
  toggleEmpty();
}
function formatDate(d){
  if(!d) return '';
  // Expecting yyyy-MM-dd
  const [y,m,day] = d.split('-');
  return `${day}/${m}/${y}`;
}
// viewOrder retained (maybe used elsewhere) but no longer linked in UI
function viewOrder(orderId){ if(orderId) window.location.href = '/order?orderId=' + orderId; }
window.filterTickets = filterTickets;
window.viewOrder = viewOrder;
document.addEventListener('DOMContentLoaded', initTickets);
